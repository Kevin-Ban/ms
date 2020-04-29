package com.stock.controller;

import bean.result.Result;
import com.stock.bean.Stock;
import com.stock.service.CacheService;
import com.stock.service.MemoryCacheStock;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "缓存管理")
@RestController
@RequestMapping("/cache")
public class CacheManageController {

    @Autowired
    private CacheService cacheService;

    @ApiOperation("清理库存的缓存")
    @PostMapping("/clearStock")
    public Result clearStock() {
        cacheService.clearStock();
        return Result.success();
    }

    @ApiOperation("刷新库存的缓存")
    @PostMapping("/refreshStock")
    public Result refreshStock() {
        cacheService.refreshStock();
        return Result.success(MemoryCacheStock.getInstance());
    }

    @ApiOperation("获取库存的缓存数据")
    @PostMapping("/listStock")
    public Result<List> listStock() {
        List<Stock> list = cacheService.listStock();
        return Result.success(list);
    }
}
