package com.order.mapper;

import com.order.bean.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper {

    /**
     * sql语句使用replace into，在主键或者唯一索引发生冲突时，替换原来的数据，而不是新增
     * 修改：此处应使用on duplicate key update , 因为使用replace into会产生死锁
     *
     * @param param
     * @return
     */
    @Insert("    insert INTO `order`\n" +
            "    (`user_id`, `stock_code`, `pay_flag`, `purch_time`, `purch_price`, `expired_time`, `order_id`)\n" +
            "    VALUES (#{userId}, #{stockCode}, #{payFlag}, #{purchTime}, #{purchPrice}, #{expiredTime}, #{orderId})" +
            " on duplicate key update user_id = #{userId}, stock_code=#{stockCode}, pay_flag=#{payFlag}, purch_time=#{purchTime}," +
            " purch_price=#{purchPrice}, expired_time=#{expiredTime}")
    int duplicateUpdate(Order param);
}
