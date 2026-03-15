package com.example.board.post.security;

import com.example.board.post.commons.response.ApiResponse;
import com.example.board.post.commons.response.CommonErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.util.LinkedHashMap;

import static com.example.board.post.commons.utils.BearerHeaderUtils.computeWWWAuthenticateHeaderValue;

@Component
@RequiredArgsConstructor
public class BearerTokenAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final JsonMapper jsonMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        var errorMessage = authException.getMessage();
        var code = BearerTokenErrorMessages.TOKEN_EXPIRED.equals(errorMessage) ? CommonErrorCode.TOKEN_EXPIRED : CommonErrorCode.TOKEN_INVALID;

        var parameters = new LinkedHashMap<String, String>();
        parameters.put("error", BearerTokenErrorCodes.INVALID_TOKEN);
        parameters.put("error_description", errorMessage);

        var wwwAuthenticate = computeWWWAuthenticateHeaderValue(parameters);

        response.setStatus(code.getHttpStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.addHeader("WWW-Authenticate", wwwAuthenticate);

        var body = ApiResponse.error(code);
        jsonMapper.writeValue(response.getWriter(), body);
    }
}
