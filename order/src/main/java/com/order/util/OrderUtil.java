package com.order.util;

import org.springframework.beans.factory.annotation.Value;

public class OrderUtil {

    /**
     * 订单过期时间，默认为15分钟
     */
    @Value("order.expiredTime")
    public static Long expiredTime;
}
