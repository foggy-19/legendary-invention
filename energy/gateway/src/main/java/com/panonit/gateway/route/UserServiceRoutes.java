package com.panonit.gateway.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;

@Configuration
public class UserServiceRoutes {

    private static final String ID = "user-service";
    private static final String URL = "http://localhost:8080";
    private static final String PATH = "/api/v1/user/**";

    @Bean
    public RouterFunction<ServerResponse> energyUserServiceRoute() {
        return route(ID).route(RequestPredicates.path(PATH), http()).before(uri(URL)).build();
    }
}
