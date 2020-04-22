package com.stock.service;

import com.stock.mapper.StockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class StockService {

    @Autowired
    private StockMapper stockMapper;

    public List<Map<String, Object>> list() {
        return stockMapper.query();
    }
}
