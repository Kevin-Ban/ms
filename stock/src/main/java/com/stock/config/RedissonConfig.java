package com.stock.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private String port;

    @Value("${spring.redis.password}")
    private String password;

    @Bean
    public RedissonClient redissonLock() {
        RedissonClient lock;
        Config config = new Config();
        if (password != null && !password.isEmpty()) {
            config.useSingleServer().setAddress("redis://" + host + ":" + port).setPassword(password);
        } else {
            config.useSingleServer().setAddress("redis://" + host + ":" + port);
        }
        lock = Redisson.create(config);
        return lock;
    }
}
