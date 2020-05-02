package bean.message;

import bean.User;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderMessage {

    /**
     * 订单id
     */
    private long orderId;

    /**
     * 用户信息
     */
    private User user;

    /**
     * 商品库存编码
     */
    private String stockCode;

    /**
     * 商品价格
     */
    private BigDecimal price;
}
