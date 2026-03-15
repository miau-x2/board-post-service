package com.example.board.post.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.jwt")
public record TokenProperties(String issuer, String publicKeyPem) {
}
