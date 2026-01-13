package com.sinha.trading_app.auth_service.config;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GatewayAuthenticationFilter extends OncePerRequestFilter {

    @Value("${api-gateway.secret-key:}")
    private String apiGatewaySecretKey;

    private static final List<String> BYPASS_PATHS = List.of(
        "/error"
    );


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws IOException, ServletException {

        String path = request.getRequestURI();

//        // Bypass authentication for specific paths
//        if (BYPASS_PATHS.stream().anyMatch(path::startsWith)) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        if (apiGatewaySecretKey == null || apiGatewaySecretKey.isEmpty()) {
//            response.sendError(javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "API Gateway secret key is not configured");
//            return;
//        }
//
//        if (secretKey == null || !secretKey.equals(apiGatewaySecretKey)) {
//            response.sendError(javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: Invalid API Key");
//            return;
//        }

        filterChain.doFilter(request, response);
    }

}
