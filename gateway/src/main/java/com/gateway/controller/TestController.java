package com.gateway.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {

    @PostMapping("/testGateway")
    public Map<String, Object> testGateway(@RequestBody Map<String, Object> param) {
        Map<String, Object> result = new HashMap<>(16);
        result.put("isSuccess", true);
        result.put("param", param);
        return result;
    }
}
