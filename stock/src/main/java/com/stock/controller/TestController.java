package com.stock.controller;

import com.stock.service.StockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class TestController {

    @Autowired
    private StockService stockService;

    @GetMapping("/test")
    public Map<String, Object> test() {
        Map<String, Object> result = new HashMap<>(16);
        result.put("test", "success");
        result.put("list", stockService.list());
        log.info("result:" + result.toString());
        return result;
    }
}
