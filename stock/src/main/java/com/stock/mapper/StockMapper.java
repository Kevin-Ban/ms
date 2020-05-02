package com.stock.mapper;

import com.stock.bean.Stock;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface StockMapper {

    @Select("select * from stock")
    List<Stock> query();

    @Update("update stock set total = total - 1 where code = #{code} and total > 0")
    int decreaseStock(@Param("code") String code);
}
