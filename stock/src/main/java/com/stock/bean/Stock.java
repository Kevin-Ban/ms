package com.stock.bean;

import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class Stock {

    @Min(value = 1, message = "参数必须大于0")
    private Integer id;

    private String name;

    private String code;

    @Min(value = 0, message = "参数必须大于等于0")
    private Integer total;
}
