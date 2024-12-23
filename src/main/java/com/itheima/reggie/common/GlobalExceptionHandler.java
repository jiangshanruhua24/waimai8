package com.itheima.reggie.common;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody //返回JSON数据
@Slf4j
public class GlobalExceptionHandler {
    /**
     * SQLIntegrityConstraintViolationException处理
     * @param e
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException e) {
        log.error(e.getMessage());

        if (e.getMessage().contains("Duplicate entry")) {
            String split = e.getMessage().split(" ")[2];
            String message = split+"已存在";
            return R.error(message);
        }

        return R.error(e.getMessage());
    }
}
