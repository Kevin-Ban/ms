package com.stock.mapper;

import com.stock.bean.Stock;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StockMapper {

    @Select("select * from stock")
    List<Stock> query();

    int decreaseStock(@Param("code") String code);
}
