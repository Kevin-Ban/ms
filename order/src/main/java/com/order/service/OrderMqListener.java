package com.order.service;

import bean.message.OrderMQMessage;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
public class OrderMqListener implements MessageListenerConcurrently {
    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        for (MessageExt item : list) {
            String json = new String(item.getBody(), StandardCharsets.UTF_8);
            log.info("接收到的订单消息为：" + json);
            OrderMQMessage orderMqMessage = JSONObject.parseObject(json, OrderMQMessage.class);
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
