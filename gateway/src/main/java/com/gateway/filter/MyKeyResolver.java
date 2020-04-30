package com.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
public class MyKeyResolver implements KeyResolver {

    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        log.info("hostName" + exchange.getRequest().getRemoteAddress().getHostName());
        Mono<String> just = Mono.just(exchange.getRequest().getURI().getPath());
        return just;
    }
}
