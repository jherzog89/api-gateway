package com.herzog.microservices.api_gateway;


import org.junit.jupiter.api.Test;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;

import com.herzog.microservices.api_gateway.configuration.ApiGatewayConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


class ApiGatewayConfigurationTest {

    @Test
    void gatewayRouter_createsRoutes() {
        // Mock RouteLocatorBuilder and its fluent API
        RouteLocatorBuilder builder = mock(RouteLocatorBuilder.class);
        RouteLocatorBuilder.Builder routesBuilder = mock(RouteLocatorBuilder.Builder.class);
        RouteLocator routeLocator = mock(RouteLocator.class);

        when(builder.routes()).thenReturn(routesBuilder);
        when(routesBuilder.route(any())).thenReturn(routesBuilder);
        when(routesBuilder.build()).thenReturn(routeLocator);

        ApiGatewayConfiguration config = new ApiGatewayConfiguration();
        RouteLocator locator = config.gatewayRouter(builder);

        assertThat(locator).isNotNull();
        verify(builder).routes();
        verify(routesBuilder, atLeastOnce()).route(any());
        verify(routesBuilder).build();
    }
}