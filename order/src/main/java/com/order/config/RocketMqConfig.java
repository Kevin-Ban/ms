package com.order.config;

import com.alibaba.fastjson.JSONException;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;

@Configuration
@Slf4j
public class RocketMqConfig {

    @Value("${rocketmq.consumer.groupName}")
    private String groupName;
    @Value("${rocketmq.consumer.namesrvAddr}")
    private String namesrvAddr;
    @Value("${rocketmq.consumer.instanceName}")
    private String instanceName;
    @Value("${rocketmq.producer.sendMsgTimeout}")
    private int sendMsgTimeout;
    @Value("${rocketmq.producer.maxMessageSize}")
    private int maxMessageSize;
    @Value("${rocketmq.topic}")
    private String topic;
    @Value("${rocketmq.tag}")
    private String tag;

    @Bean
    public DefaultMQPushConsumer getRocketMQConsumer() throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(this.groupName);
        consumer.setNamesrvAddr(this.namesrvAddr);
        consumer.setInstanceName(this.instanceName);
        consumer.setConsumeMessageBatchMaxSize(1);
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        //单队列并行消费最大跨度，用于流控
        consumer.setConsumeConcurrentlyMaxSpan(2000);
        // 一个queue最大消费的消息个数，用于流控
        consumer.setPullThresholdForQueue(1000);
        //消息拉取时间间隔，默认为0，即拉完一次立马拉第二次，单位毫秒
        //consumer.setPullInterval(1000);
        //消费模式，集群消费
        consumer.setMessageModel(MessageModel.CLUSTERING);
        try {
//            consumer.subscribe(this.topic, tag);
            consumer.subscribe("test", "*");
            consumer.registerMessageListener((MessageListenerConcurrently) (msgList, consumeConcurrentlyContext) -> {
                try {
                    MessageExt msg = null;
                    for (MessageExt aMsgList : msgList) {
                        msg = aMsgList;
                        log.info("收到MQ消息：" + new String(aMsgList.getBody(), StandardCharsets.UTF_8));
                    }
                } catch (JSONException e) {
                    log.error("", e);
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            });
            consumer.start();
            log.info("已启动Consumer【group:" + this.groupName + "，instance:" + this.instanceName
                    + "】，监听TOPIC-{" + this.topic + "},tag-{" + this.tag + "}");
        } catch (MQClientException e) {
            log.error("", e);
        }
        return consumer;
    }
}
