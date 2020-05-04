package com.gateway.config;

import com.gateway.filter.LimitFilter;
import com.gateway.filter.LoginFilter;
import com.gateway.filter.MyKeyResolver;
import com.gateway.filter.RedisLimitFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class FilterConfig {
    @Bean
    public LoginFilter loginFilter() {
        return new LoginFilter();
    }

    //    @Bean
    public LimitFilter limitFilter() {
        return new LimitFilter();
    }

    @Bean(name = "myKeyResolver")
    public MyKeyResolver myKeyResolver() {
        return new MyKeyResolver();
    }

    //    @Bean
    public RedisLimitFilter redisLimitFilter() {
        return new RedisLimitFilter();
    }
}
