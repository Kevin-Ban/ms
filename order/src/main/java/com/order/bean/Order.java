package com.order.bean;

import bean.message.OrderMessage;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.order.util.OrderUtil;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Data
public class Order {

    private int id;

    @NotNull
    private String userId;

    private String stockCode;

    @Min(value = 0, message = "参数不能小于0")
    @NotNull
    private int payFlag;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GTM+8")
    private Date purchTime;

    private BigDecimal purchPrice;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GTM+8")
    private Date expiredTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GTM+8")
    private Date createTime;

    private long orderId;

    public static Order fromOrderMq(OrderMessage orderMessage) {
        Order order = new Order();
        order.setOrderId(orderMessage.getOrderId());
        order.setStockCode(orderMessage.getStockCode());
        LocalDateTime now = LocalDateTime.now();
        Date purchTime = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
        now = now.plusMinutes(Optional.ofNullable(OrderUtil.expiredTime).orElse(15L));
        order.setPurchTime(purchTime);
        Date expiredTime = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
        order.setExpiredTime(expiredTime);
        order.setPayFlag(0);
        order.setPurchPrice(orderMessage.getPrice());
        order.setUserId(orderMessage.getUser().getUserId());
        return order;
    }
}
