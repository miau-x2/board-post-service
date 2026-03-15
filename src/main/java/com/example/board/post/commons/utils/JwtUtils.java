package com.example.board.post.commons.utils;

import com.example.board.post.config.TokenProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.PublicKey;

@Component
@RequiredArgsConstructor
public class JwtUtils {
    private final PublicKey publicKey;
    private final TokenProperties tokenProperties;

    public Claims getClaims(String accessToken) {
        return Jwts.parser()
                .verifyWith(publicKey)
                .requireIssuer(tokenProperties.issuer())
                .build()
                .parseSignedClaims(accessToken)
                .getPayload();
    }
}
