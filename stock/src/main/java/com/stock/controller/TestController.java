package com.stock.controller;

import com.stock.bean.Stock;
import com.stock.service.StockService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class TestController {

    @Autowired
    private StockService stockService;
    @Autowired
    private DefaultMQProducer producer;

    @GetMapping("/test")
    public Map<String, Object> test() {
        Map<String, Object> result = new HashMap<>(16);
        result.put("test", "success");
        result.put("list", stockService.list());
        log.info("result:" + result.toString());
        return result;
    }

    @PostMapping("/testProducer")
    public Map<String, Object> testProducer(@RequestBody @Validated Stock param) throws UnsupportedEncodingException, InterruptedException, RemotingException, MQClientException, MQBrokerException {
        Message msg = new Message("test", "测试测试", param.toString().getBytes(RemotingHelper.DEFAULT_CHARSET));
        producer.send(msg);
//        param.put("isSuccess", true);
        Map<String, Object> result = new HashMap<>(16);
        result.put("isSuccess", true);
        result.put("param", param);
        return result;
    }
}
