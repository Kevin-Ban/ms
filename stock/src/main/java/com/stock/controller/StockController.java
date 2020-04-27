package com.stock.controller;

import com.stock.bean.Stock;
import com.stock.bean.result.Result;
import com.stock.service.MemoryCacheStock;
import com.stock.service.StockService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@Slf4j
@Api("内存地址标志管理")
public class StockController {

    // region 内存地址标志
    @Autowired
    private StockService stockService;

    @PostConstruct
    private void refreshMemoryCache() {
        log.info("刷新化库存内存地址标志：开始");
        ConcurrentHashMap<String, Boolean> stockMap = MemoryCacheStock.getInstance();
        List<Stock> list = stockService.listAll();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        for (Stock item : list) {
            if (item.getTotal() > 0) {
                stockMap.put(item.getCode(), true);
            } else {
                stockMap.put(item.getCode(), false);
            }
        }
        log.info("刷新化库存内存地址标志：完成");
    }

    @PostMapping("/refreshCache")
    @ApiOperation("刷新内存地址标志")
    public Result refreshCache() {
        refreshMemoryCache();
        return Result.success(MemoryCacheStock.getInstance());
    }
    //endregion
}
