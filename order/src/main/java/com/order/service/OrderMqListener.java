package com.order.service;

import bean.message.OrderMessage;
import com.alibaba.fastjson.JSONObject;
import com.order.bean.Order;
import com.order.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Service
public class OrderMqListener implements MessageListenerConcurrently {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {

        for (MessageExt item : list) {
            try {
                String json = new String(item.getBody(), StandardCharsets.UTF_8);
                log.info("接收到的订单消息为：" + json);
                OrderMessage orderMessage = JSONObject.parseObject(json, OrderMessage.class);
                Order order = Order.fromOrderMq(orderMessage);
                int result = orderMapper.replaceIntoOrder(order);
                if (result <= 0) {
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
            } catch (Exception e) {
                log.error("消费失败, topic:" + item.getTopic() + "tag:" + item.getTags(), e);
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            } finally {

            }
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
