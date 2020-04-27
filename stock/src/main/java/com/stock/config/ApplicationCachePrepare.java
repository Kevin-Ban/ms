package com.stock.config;

import com.stock.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ApplicationCachePrepare implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private CacheService cacheService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        log.info("spring容器启动完毕，开始准备缓存");
        cacheService.refreshStock();
        log.info("缓存准备完毕");
    }
}
