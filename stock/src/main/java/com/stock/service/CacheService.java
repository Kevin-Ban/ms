package com.stock.service;

import com.stock.bean.Stock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import util.Global;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class CacheService {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private StockService stockService;

    public long clearStock() {
        String pattern = Global.STOCK_CACHE + "*";
        return refreshCache(pattern);
    }

    /**
     * 刷新库存
     */
    public void refreshStock() {
        log.info("刷新redis和内存地址缓存：开始");
        List<Stock> list = stockService.listAll();
        ConcurrentHashMap<String, Boolean> stockMap = MemoryCacheStock.getInstance();
        for (Stock item : list) {
            stockMap.put(item.getCode(), item.getTotal() > 0);
            redisTemplate.opsForValue().set(Global.STOCK_CACHE + item.getCode(), item);
        }
        log.info("刷新redis和内存地址缓存：完成");
    }

    public List<Stock> listStock() {
        String pattern = Global.STOCK_CACHE + "*";
        List<Object> list = listCache(pattern);
        if (!CollectionUtils.isEmpty(list)) {
            List<Stock> result = new ArrayList<>(list.size());
            for (int i = 0; i < list.size(); i++) {
                result.add((Stock) list.get(i));
            }
            return result;
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * 清理缓存
     *
     * @param pattern
     * @return
     */
    private long refreshCache(String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        return redisTemplate.delete(keys);
    }

    private List<Object> listCache(String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        if (!CollectionUtils.isEmpty(keys)) {
            List<Object> result = new ArrayList(keys.size());
            for (String item : keys) {
                Object value = redisTemplate.opsForValue().get(item);
                result.add(value);
            }
            return result;
        }
        return Collections.emptyList();
    }
}
