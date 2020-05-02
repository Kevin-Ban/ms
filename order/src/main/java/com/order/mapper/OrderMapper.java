package com.order.mapper;

import com.order.bean.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper {

    /**
     * sql语句使用replace into，在主键或者唯一索引发生冲突时，替换原来的数据，而不是新增
     *
     * @param param
     * @return
     */
    int replaceIntoOrder(Order param);
}
