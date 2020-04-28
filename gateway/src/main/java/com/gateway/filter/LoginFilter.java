package com.gateway.filter;

import com.alibaba.fastjson.JSONObject;
import com.gateway.bean.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 登陆过滤器，用户未登录，则直接返回
 */
public class LoginFilter implements GlobalFilter, Ordered {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // 在header中检查用户是否已登陆
        HttpHeaders headers = exchange.getRequest().getHeaders();
        if (!CollectionUtils.isEmpty(headers)) {
            List<String> tokens = headers.get("token");
            if (!CollectionUtils.isEmpty(tokens)) {
                String token = tokens.get(0);
                if (token != null && !token.isEmpty()) {
                    if (userIsLogin(token)) {
                        return chain.filter(exchange);
                    }
                }
            }
        }
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        Result result = Result.fail("用户未登录");
        byte[] bits = JSONObject.toJSONString(result).getBytes(StandardCharsets.UTF_8);
        ServerHttpResponse response = exchange.getResponse();
        DataBuffer buffer = response.bufferFactory().wrap(bits);
        response.getHeaders().add("Content-Type", "text/json;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }

    private boolean userIsLogin(String token) {
        Object data = redisTemplate.opsForValue().get(token);
        return data != null;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
