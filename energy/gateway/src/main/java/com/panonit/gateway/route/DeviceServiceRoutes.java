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
public class DeviceServiceRoutes {

    private static final String ID = "device-service";
    private static final String URL = "http://localhost:8081";
    private static final String PATH = "/api/v1/device/**";

    @Bean
    public RouterFunction<ServerResponse> energyDeviceServiceRoute() {
        return route(ID).route(RequestPredicates.path(PATH), http()).before(uri(URL)).build();
    }
}
