package com.stock.controller;

import bean.message.OrderMessage;
import bean.result.Result;
import bean.result.ResultMsg;
import com.stock.service.MemoryCacheStock;
import com.stock.service.StockService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import util.Global;
import util.SnowFlake;

@RestController
@Slf4j
@Api("库存管理")
public class StockController {

    @Autowired
    private StockService stockService;
    @Autowired
    private RedisTemplate redisTemplate;

    @ApiOperation("减库存接口")
    @PostMapping("/ms/{code}")
    public Result ms(@PathVariable("code") String code) {

        // 如果内存地址中不包含code，则商品不存在或已下架
        if (!MemoryCacheStock.getInstance().containsKey(code)) {
            return Result.fail(ResultMsg.GOOD_NOT_EXIST);
        }
        // 如果内存地址中的商品code为false，则表明库存不足
        if (!MemoryCacheStock.getInstance().get(code)) {
            return Result.fail(ResultMsg.GOOD_NOT_ENOUGH);
        }
        // 如果redis中的库存小于等于0，则表明库存不足
        int total = (int) redisTemplate.opsForValue().get(Global.STOCK_CACHE + code);
        if (total <= 0) {
            return Result.fail(ResultMsg.GOOD_NOT_ENOUGH);
        }
        // 如果redis减库存，如果库存不足，则修改地址标志
        total = total - 1;
        if (total <= 0) {
            MemoryCacheStock.getInstance().put(code, false);
        }
        redisTemplate.opsForValue().set(Global.STOCK_CACHE + code, total);
        int result = stockService.decreaseStock(code);
        // 如果扣减库存失败，则修改内存地址的值
        if (result < 0) {
            MemoryCacheStock.getInstance().put(code, false);
            return Result.fail(ResultMsg.SYSTEM_ERROR);
        }
        OrderMessage orderMessage = new OrderMessage();
        orderMessage.setOrderId(SnowFlake.nextId());
        orderMessage.setStockCode(code);
        // region 此处应该填入用户信息，或者至少应该填入token的信息，让检索用户的操作延后到订单服务
        orderMessage.setUser(null);
        // endregion
        // region 发送RocketMq消息

        // endregion
        return null;
    }

}
