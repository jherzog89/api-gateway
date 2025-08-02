package com.herzog.microservices.api_gateway.configuration;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGatewayConfiguration {

    @Bean
    public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path("/authenticate", "/public/**") // Include /public-key/base64
                        .filters(f -> f.stripPrefix(0)) // Keep the path intact
                        .uri("lb://authentication-service"))
                .route(p -> p
                        .path("/reverseString", "/hello-world-bean/**")
                        .filters(f -> f.stripPrefix(0)) // Keep /reverseString
                        .uri("lb://reverse-string-service"))
                .route(p -> p
                        .path("/reverseWords")
                        .filters(f -> f.stripPrefix(0)) // Keep /reverseString
                        .uri("lb://reverse-words-service"))




                        
                .build();
    }
}
