package com.example.board.post.config;

import com.example.board.post.commons.utils.PemUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class JwtKeyConfig {
    private final TokenProperties tokenProperties;

    @Bean
    public PublicKey publicKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        return PemUtils.getPublicKey(tokenProperties.publicKeyPem());
    }
}
