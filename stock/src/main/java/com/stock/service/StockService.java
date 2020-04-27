package com.stock.service;

import com.stock.bean.Stock;
import com.stock.mapper.StockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockService {

    @Autowired
    private StockMapper stockMapper;

    public List<Stock> listAll() {
        return stockMapper.query();
    }
}
