package com.herzog.microservices.api_gateway.jwtverify;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
@EnableWebFluxSecurity
public class GatewaySecurityConfig {

    private final WebClient webClient;
    private final String publicKeyUrl;

    public GatewaySecurityConfig(WebClient.Builder webClientBuilder,
                                @Value("${auth.service.public-key-url}") String publicKeyUrl) {
        this.webClient = webClientBuilder.build();
        this.publicKeyUrl = publicKeyUrl;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        System.out.println("in securityChain");
        return http
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/authenticate", "/public/**", "/actuator/**").permitAll() // Allow public access
                        .pathMatchers("/reverseString", "/hello-world-bean/**").authenticated() // Require JWT for /reverseString
                        .anyExchange().permitAll()) // Allow other routes (adjust as needed)
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtDecoder(jwtDecoder())))
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                 .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .build();
    }

        @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*");
        //configuration.addAllowedOrigin("http://localhost:4200"); // TODO Allow Angular frontend
        configuration.addAllowedMethod("*"); // Allow GET, POST, etc.
        configuration.addAllowedHeader("*"); // Allow all headers (e.g., Authorization)
        configuration.setAllowCredentials(false); // Allow cookies, if needed TODO
        configuration.setMaxAge(3600L); // Cache preflight response for 1 hour

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
                System.out.println("in jwtDecoder");

        return Mono.fromCallable(() -> fetchPublicKey())
                .flatMap(publicKey -> Mono.just(NimbusReactiveJwtDecoder.withPublicKey(publicKey).build()))
                .block();
    }

    private RSAPublicKey fetchPublicKey() {
        try {
                    System.out.println("in fetchPublicKey");

            String base64Key = webClient.get()
                    .uri(publicKeyUrl)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            System.out.println("base64 key is: " + base64Key);
            byte[] keyBytes = Base64.getDecoder().decode(base64Key);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to fetch or parse RSA public key", e);
        }
    }
}