package org.example.client.handler;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.example.common.exception.BaseException;
import org.example.common.result.Result;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理控制器，处理控制器（Controller层）中抛出的异常
 */
@Slf4j
@RestControllerAdvice //定义一个全局的异常处理类
public class GlobalExceptionHandler {

    /**
     * 捕获和处理特定的异常类型
     *
     * @param e 业务异常
     * @return 响应提示信息
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException e) {
        log.error("[log] 异常信息：{}", e.getMessage());
        return Result.error(e.getMessage());
    }

    @ExceptionHandler
    public Result exceptionHandler(MethodArgumentNotValidException e) {
        log.error("[log] 异常信息：{}", e.getFieldError().getField() + e.getFieldError().getDefaultMessage());
        return Result.error(e.getFieldError().getField() + e.getFieldError().getDefaultMessage());
    }

    @ExceptionHandler
    public Result exceptionHandler(ConstraintViolationException e) {
        log.error("[log] 异常信息：{}", e.getMessage());
        return Result.error(e.getMessage());
    }

}
