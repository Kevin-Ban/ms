package com.stock.service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 内存地址标志
 */
public class MemoryCacheStock {

    private volatile static ConcurrentHashMap<String, Boolean> stockCache;

    private static Boolean flag = true;

    public static ConcurrentHashMap<String, Boolean> getInstance() {
        if (stockCache == null) {
            synchronized (flag) {
                if (stockCache == null) {
                    stockCache = new ConcurrentHashMap<>(16);
                }
                return stockCache;
            }
        }
        return stockCache;
    }
}
