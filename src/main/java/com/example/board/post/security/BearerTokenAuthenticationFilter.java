package com.example.board.post.security;

import com.example.board.post.commons.utils.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BearerTokenAuthenticationFilter extends OncePerRequestFilter {
    private static final String BEARER_PREFIX = "Bearer ";
    private final JwtUtils jwtUtils;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var authorization = request.getHeader("Authorization");
        var accessToken = extractAccessToken(authorization);
        if(accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            var claims = jwtUtils.getClaims(accessToken);
            var subject = claims.getSubject();
            if(subject == null || subject.isBlank()) {
                throw new JwtException("유효하지 않은 sub입니다.");
            }
            var role = claims.get("role", String.class);
            if(!"ADMIN".equals(role) && !"USER".equals(role)) {
                throw new JwtException("유효하지 않은 role입니다.");
            }
            var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));
            var authentication = BearerAuthenticationToken.authenticated(subject, null, authorities);

            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            SecurityContextHolder.clearContext();
            authenticationEntryPoint.commence(
                    request,
                    response,
                    new InsufficientAuthenticationException(BearerTokenErrorMessages.TOKEN_EXPIRED, e)
            );

        } catch (JwtException e) {
            SecurityContextHolder.clearContext();
            authenticationEntryPoint.commence(
                    request,
                    response,
                    new InsufficientAuthenticationException(BearerTokenErrorMessages.TOKEN_INVALID, e)
            );
        }
    }

    private String extractAccessToken(String authorization) {
        if(authorization == null || !authorization.startsWith(BEARER_PREFIX)) {
            return null;
        }
        var token = authorization.substring(BEARER_PREFIX.length());
        return token.isBlank() ? null : token;
    }
}
