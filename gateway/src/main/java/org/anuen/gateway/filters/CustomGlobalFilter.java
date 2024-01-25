package org.anuen.gateway.filters;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // todo simulate check login
        HttpHeaders headers = exchange.getRequest().getHeaders();
        System.out.println("headers = " + headers);
        // access
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        // smaller number has higher priority
        return 0;
    }
}
