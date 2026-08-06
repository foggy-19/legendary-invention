package com.panonit.gateway.route;

import org.springframework.cloud.gateway.server.mvc.filter.CircuitBreakerFilterFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;
import static org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions.setPath;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;

@Configuration
public class UserServiceRoutes {

    private static final String ROUTE_ID = "user-service";
    private static final String URL = "http://localhost:8080";
    private static final String PATH = "/api/v1/user/**";

    private static final String DOCS_ROUTE_ID = "user-service-docs";
    private static final String DOCS_PATH = "/api-docs/v1/user";
    private static final String DOCS_FW_PATH = "/v3/api-docs";

    private static final String FALLBACK_ROUTE_ID = "user-service-fallback";
    private static final String FALLBACK_PATH = "/fallback/user";
    private static final String ERROR_MESSAGE = "User service is down";

    @Bean
    public RouterFunction<ServerResponse> energyUserServiceRoute() {
        return route(ROUTE_ID)
                .route(RequestPredicates.path(PATH), http())
                .before(uri(URL))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("userServiceCircuitBraker", URI.create("forward:" + FALLBACK_PATH)))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> energyUserServiceDocsRoute() {
        return route(DOCS_ROUTE_ID)
                .route(RequestPredicates.path(DOCS_PATH), http())
                .before(uri(URL))
                .filter(setPath(DOCS_FW_PATH))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> energyUserFallbackRoute() {
        return route(FALLBACK_ROUTE_ID)
                .route(RequestPredicates.path(FALLBACK_PATH), request -> ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE).body(ERROR_MESSAGE))
                .build();
    }
}
