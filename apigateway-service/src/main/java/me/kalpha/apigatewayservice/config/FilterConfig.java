package me.kalpha.apigatewayservice.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class FilterConfig {
//    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/first-service/**")
                        .filters(f -> f.addRequestHeader("first-request", "first-service-request")
                                       .addResponseHeader("first-response", "first-service-response"))
                        .uri("http://localhost:8001"))
                .route((r -> r.path("/second-service/**")
                        .filters(f -> f.addRequestHeader("second-request", "second-service-request")
                                       .addResponseHeader("second-response", "second-service-request")
                        )
                        .uri("http://localhost:8002")
                ))
                .build();
    }
}
