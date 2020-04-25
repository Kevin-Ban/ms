package com.order.controller;

import com.order.bean.Order;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {

    @PostMapping("/testOrder")
    public Map<String, Object> testOrder(@RequestBody @Validated Order param) throws UnsupportedEncodingException, InterruptedException, RemotingException, MQClientException, MQBrokerException {
        Message msg = new Message("test", "测试测试", param.toString().getBytes(RemotingHelper.DEFAULT_CHARSET));
//        producer.send(msg);
//        param.put("isSuccess", true);
        Map<String, Object> result = new HashMap<>(16);
        result.put("isSuccess", true);
        result.put("param", param);
        return result;
    }
}
