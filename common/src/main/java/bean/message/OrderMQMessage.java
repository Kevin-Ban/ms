package bean.message;

import com.alibaba.fastjson.JSONObject;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import util.Global;

import java.io.UnsupportedEncodingException;

public class OrderMQMessage extends Message {

    public OrderMQMessage(byte[] body) {
        super();
        this.setTopic(Global.MQ_ORDER_TOPIC);
        this.setTags(Global.MQ_ORDER_TAG);
        this.setBody(body);
    }

    public OrderMQMessage(Object data) throws UnsupportedEncodingException {
        super();
        this.setTopic(Global.MQ_ORDER_TOPIC);
        this.setTags(Global.MQ_ORDER_TAG);
        String dataString = JSONObject.toJSONString(data);
        super.setBody(dataString.getBytes(RemotingHelper.DEFAULT_CHARSET));
    }
}
