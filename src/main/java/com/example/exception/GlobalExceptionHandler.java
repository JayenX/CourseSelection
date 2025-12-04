package com.example.exception;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.example.common.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


@ControllerAdvice(basePackages = "com.example.controller")
public class GlobalExceptionHandler {

    private static final Log log = LogFactory.get();


    //The unified exception handling @ExceptionHandler is mainly used for exceptions
    @ExceptionHandler(Exception.class)
    @ResponseBody//Returns a JSON string
    public Result error(HttpServletRequest request, Exception e) {
        log.error("Exception information：", e);
        return Result.error();
    }

    @ExceptionHandler(CustomException.class)
    @ResponseBody//Returns a JSON string
    public Result customError(HttpServletRequest request, CustomException e) {
        return Result.error(e.getMsg());
    }

}
