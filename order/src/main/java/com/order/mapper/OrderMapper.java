package com.order.mapper;

import com.order.bean.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper {

    /**
     * sql语句使用replace into，在主键或者唯一索引发生冲突时，替换原来的数据，而不是新增
     *
     * @param param
     * @return
     */
    @Insert("    replace INTO `order`\n" +
            "    (`user_id`, `stock_code`, `pay_flag`, `purch_time`, `purch_price`, `expired_time`, `order_id`)\n" +
            "    VALUES (#{userId}, #{stockCode}, #{payFlag}, #{purchTime}, #{purchPrice}, #{expiredTime}, #{orderId})")
    int replaceIntoOrder(Order param);
}
