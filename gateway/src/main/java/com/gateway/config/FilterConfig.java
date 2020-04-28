package com.gateway.config;

import com.gateway.filter.LoginFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    @Bean
    public LoginFilter loginFilter() {
        return new LoginFilter();
    }

}
