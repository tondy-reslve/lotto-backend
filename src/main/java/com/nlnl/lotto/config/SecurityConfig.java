package com.nlnl.lotto.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.web.client.RestTemplate;

@Configuration
public class SecurityConfig {

    // 讀取 application.yml 裡的 issuer-uri
    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

    @Bean
    public JwtDecoder jwtDecoder(RestTemplateBuilder builder, SslBundles sslBundles) {
        // 1. 讓 RestTemplate 吃下你在 application.yml 定義的 keycloak-bundle
        RestTemplate restTemplate = builder
                .setSslBundle(sslBundles.getBundle("keycloak-bundle"))
                .build();
        
        // 2. 讓 JwtDecoder 透過這個帶有 K8s-Local-CA 的 RestTemplate 去連 Keycloak
        return NimbusJwtDecoder.withIssuerLocation(issuerUri)
                .restOperations(restTemplate)
                .build();
    }
}