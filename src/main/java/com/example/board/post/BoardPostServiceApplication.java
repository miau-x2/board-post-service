package com.example.board.post;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@ConfigurationPropertiesScan
@EnableMethodSecurity
@EnableJpaAuditing
@EnableDiscoveryClient
@SpringBootApplication
public class BoardPostServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BoardPostServiceApplication.class, args);
    }

}
