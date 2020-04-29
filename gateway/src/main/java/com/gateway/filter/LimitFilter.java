package com.gateway.filter;

import bean.result.Result;
import com.alibaba.fastjson.JSONObject;
import com.google.common.util.concurrent.RateLimiter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

public class LimitFilter implements GlobalFilter, Ordered {

    RateLimiter limiter = RateLimiter.create(1);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (limiter.tryAcquire()) {
            return chain.filter(exchange);
        }
        exchange.getResponse().setStatusCode(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED);
        Result result = Result.fail("当前访问人数过多，请稍后再试。");
        byte[] bits = JSONObject.toJSONString(result).getBytes(StandardCharsets.UTF_8);
        ServerHttpResponse response = exchange.getResponse();
        DataBuffer buffer = response.bufferFactory().wrap(bits);
        response.getHeaders().add("Content-Type", "text/json;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
