package org.anuen.gateway.filters;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.anuen.common.exception.UnauthorizedException;
import org.anuen.gateway.config.AuthProperties;
import org.anuen.gateway.utils.JwtTool;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(AuthProperties.class)
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    private final AuthProperties properties;

    private final JwtTool jwtTool;

    private final AntPathMatcher antPathMatcher;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        if (isExcludePath(request.getPath().toString())) {
            return chain.filter(exchange); // if request path in excludePath, access
        }

        String authHeader = null;
        List<String> headers = request.getHeaders().get("authorization"); // get headers named 'authorization', then get token from headers
        if (CollectionUtil.isNotEmpty(headers)) {
            authHeader = headers.get(0);
        }

        if (StrUtil.isBlank(authHeader) || !authHeader.startsWith("Bearer ")) {
            return deny(exchange); // if token is blank and didn't start with 'Bearer '
        }

        String userUid;
        try {
            String token = authHeader.substring(7); // parse token extract userUid
            userUid = jwtTool.parseToken(token);
        } catch (UnauthorizedException e) {
            return deny(exchange); // if throw unauthorized exception, deny request
        }

        String stableUid = userUid; // transform user info to lower service
        ServerWebExchange newExchange = exchange.mutate()
                .request(builder -> builder.header("user-info", stableUid))
                .build();
        return chain.filter(newExchange);
    }

    /**
     * deny request
     *
     * @param exchange exchange
     * @return mono
     */
    private Mono<Void> deny(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }

    /**
     * check whether request path exists in exclude path
     *
     * @param path request path
     * @return yes no
     */
    private boolean isExcludePath(String path) {
        for (String pathPattern : properties.getExcludePath()) {
            if (antPathMatcher.match(pathPattern, path)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
