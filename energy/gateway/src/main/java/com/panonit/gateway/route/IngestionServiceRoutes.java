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
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;

@Configuration
public class IngestionServiceRoutes {

    private static final String ROUTE_ID = "ingestion-service";
    private static final String URL = "http://localhost:8082";
    private static final String PATH = "/api/v1/ingestion/**";

    private static final String FALLBACK_ROUTE_ID = "ingestion-service-fallback";
    private static final String FALLBACK_PATH = "/fallback/ingestion";
    private static final String ERROR_MESSAGE = "Ingestion service is down";

    @Bean
    public RouterFunction<ServerResponse> energyIngestionServiceRoute() {
        return route(ROUTE_ID)
                .route(RequestPredicates.path(PATH), http())
                .before(uri(URL))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("ingestionServiceCircuitBreaker", URI.create("forward:" + FALLBACK_PATH)))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> energyIngestionFallbackRoute() {
        return route(FALLBACK_ROUTE_ID)
                .route(RequestPredicates.path(FALLBACK_PATH), request -> ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE).body(ERROR_MESSAGE))
                .build();
    }
}
