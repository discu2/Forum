package org.discu2.forum.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@EnableDiscoveryClient
@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    RouteLocator gateway (RouteLocatorBuilder builder) {
        return builder
                .routes()
                .route(r -> r
                        .path("/account/**")
                        .uri("lb://forum-account/account/**"))
                .route(r -> r
                        .path("/file/**")
                        .uri("lb://forum-file/file/**"))
                .route(r -> r
                        .path("/**")
                        .uri("lb://forum-core/**"))
                .build();
    }
}
