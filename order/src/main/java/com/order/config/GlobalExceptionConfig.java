package com.order.config;

import bean.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@ControllerAdvice
public class GlobalExceptionConfig {

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseBody
    public Result paramValidate(MethodArgumentNotValidException e) {
        log.error("", e);
        BindingResult bindResult = e.getBindingResult();
        List<ObjectError> errors = bindResult.getAllErrors();
        List<String> errorList = new ArrayList<>(errors.size());
        for (ObjectError item : errors) {
            String s = "";
            if (item instanceof FieldError) {
                s += ((FieldError) item).getField() + ":";
            }
            s += item.getDefaultMessage();
            errorList.add(s);
        }
        return Result.fail(String.join(",", errorList));
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseBody
    public Result otherException(Exception e) {
        log.error("未知错误，请检查.", e);
        return Result.fail("未知错误");
    }
}
