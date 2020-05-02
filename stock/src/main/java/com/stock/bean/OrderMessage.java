package com.stock.bean;

import com.alibaba.fastjson.JSONObject;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import util.Global;

import java.io.UnsupportedEncodingException;

public class OrderMessage extends Message {

    public OrderMessage(byte[] body) {
        super();
        this.setTopic(Global.MQ_ORDER_TOPIC);
        this.setTags(Global.MQ_ORDER_TAG);
        this.setBody(body);
    }

    public OrderMessage(Object data) throws UnsupportedEncodingException {
        super();
        this.setTopic(Global.MQ_ORDER_TOPIC);
        this.setTags(Global.MQ_ORDER_TAG);
        String dataString = JSONObject.toJSONString(data);
        super.setBody(dataString.getBytes(RemotingHelper.DEFAULT_CHARSET));
    }
}
