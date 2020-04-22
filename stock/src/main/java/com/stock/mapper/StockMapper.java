package com.stock.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface StockMapper {

    @Select("select * from stock")
    List<Map<String, Object>> query();
}
