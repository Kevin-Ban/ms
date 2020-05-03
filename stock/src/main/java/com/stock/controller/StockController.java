package com.stock.controller;

import bean.User;
import bean.message.OrderMQMessage;
import bean.message.OrderMessage;
import bean.result.Result;
import bean.result.ResultMsg;
import com.alibaba.fastjson.JSONObject;
import com.stock.bean.Stock;
import com.stock.service.MemoryCacheStock;
import com.stock.service.StockService;
import com.stock.util.CurrentUserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import util.Global;
import util.SnowFlake;

import java.io.UnsupportedEncodingException;

@RestController
@Slf4j
@Api("库存管理")
public class StockController {

    @Autowired
    private StockService stockService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private DefaultMQProducer producer;

    @ApiOperation("减库存接口")
    @PostMapping("/ms/{code}")
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public Result ms(@PathVariable("code") String code) throws UnsupportedEncodingException, InterruptedException, RemotingException, MQClientException, MQBrokerException {

        // 如果内存地址中不包含code，则商品不存在或已下架
        if (!MemoryCacheStock.getInstance().containsKey(code)) {
            return Result.fail(ResultMsg.GOOD_NOT_EXIST);
        }
        // 如果内存地址中的商品code为false，则表明库存不足
        if (!MemoryCacheStock.getInstance().get(code)) {
            return Result.fail(ResultMsg.GOOD_NOT_ENOUGH);
        }
        // 如果redis中的库存小于等于0，则表明库存不足
        Stock stockInRedis = (Stock) redisTemplate.opsForValue().get(Global.STOCK_CACHE + code);
        if (stockInRedis == null) {
            return Result.fail(ResultMsg.GOOD_NOT_EXIST);
        }
        if (stockInRedis.getTotal() <= 0) {
            return Result.fail(ResultMsg.GOOD_NOT_ENOUGH);
        }
        // 如果redis减库存，如果库存不足，则修改地址标志
        stockInRedis.setTotal(stockInRedis.getTotal() - 1);
        if (stockInRedis.getTotal() <= 0) {
            MemoryCacheStock.getInstance().put(code, false);
        }
        redisTemplate.opsForValue().set(Global.STOCK_CACHE + code, stockInRedis);
        int result = stockService.decreaseStock(code);
        // 如果扣减库存失败，则修改内存地址的值
        if (result < 0) {
            MemoryCacheStock.getInstance().put(code, false);
            return Result.fail(ResultMsg.SYSTEM_ERROR);
        }
        OrderMessage orderMessage = new OrderMessage();
        orderMessage.setOrderId(SnowFlake.nextId());
        orderMessage.setStockCode(code);
        orderMessage.setPrice(stockInRedis.getPrice());
        // region 此处应该填入用户信息，或者至少应该填入token的信息，让检索用户的操作延后到订单服务
        User user = new User();
        user.setUserId(CurrentUserUtil.getCurrentUserId());
        orderMessage.setUser(user);
        // endregion
        // region 发送RocketMq消息
        OrderMQMessage orderMQMessage = new OrderMQMessage(orderMessage);
        log.info("发送的消息为：" + JSONObject.toJSONString(orderMQMessage));
        producer.send(orderMQMessage);
        // endregion
        return Result.success(ResultMsg.MS_SUCCESS);
    }

}
