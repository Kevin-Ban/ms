package com.stock.controller;

import bean.result.Result;
import bean.result.ResultMsg;
import com.stock.service.MemoryCacheStock;
import com.stock.service.StockService;
import com.stock.util.Global;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@Api("内存地址标志管理")
public class StockController {

    @Autowired
    private StockService stockService;
    @Autowired
    private RedisTemplate redisTemplate;

    @ApiOperation("减库存接口")
    @PostMapping("/ms/{code}")
    public Result ms(@PathVariable("code") String code) {

        // region 此处判断用户是否已登录

        // endregion

        // 如果内存地址中不包含code，则商品不存在或已下架
        if (!MemoryCacheStock.getInstance().containsKey(code)) {
            return Result.fail(ResultMsg.GOOD_NOT_EXIST);
        }
        // 如果内存地址中的商品code为false，则表明库存不足
        if (!MemoryCacheStock.getInstance().get(code)) {
            return Result.fail(ResultMsg.GOOD_NOT_ENOUGH);
        }
        int total = (int) redisTemplate.opsForValue().get(Global.STOCK_CACHE + code);
        if (total <= 0) {
            return Result.fail(ResultMsg.GOOD_NOT_ENOUGH);
        }
        return null;
    }

}
