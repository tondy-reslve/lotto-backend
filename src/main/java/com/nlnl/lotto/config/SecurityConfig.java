package com.nlnl.lotto.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.net.http.HttpClient;

@Configuration
public class SecurityConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

    @Bean
    public JwtDecoder jwtDecoder(SslBundles sslBundles) {

        SSLContext sslContext = sslBundles.getBundle("keycloak-bundle").createSslContext();
        
        HttpClient httpClient = HttpClient.newBuilder()
                .sslContext(sslContext)
                .build();
        
        RestTemplate restTemplate = new RestTemplate(new JdkClientHttpRequestFactory(httpClient));
        
        String jwkSetUri = issuerUri + "/protocol/openid-connect/certs";
        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri)
                .restOperations(restTemplate)
                .build();
    }
}