package org.example.admin.handler;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.example.common.exception.ServiceException;
import org.example.common.result.Result;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 全局异常处理器
 * "@ControllerAdvice"默认只会处理Controller层抛出的异常，
 * 如果需要处理Service层的异常，可以自定义一个BaseException来继承RuntimeException类，然后使用@ExceptionHandler捕获BaseException即可。
 */
@Slf4j
@RestControllerAdvice //定义一个全局的异常处理类
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     *
     * @param e 业务异常
     * @return 响应提示信息
     */
    @ExceptionHandler //捕获和处理特定的异常类型
    public Result<Object> exceptionHandler(ServiceException e) {
        log.error("[log] 业务异常 ServiceException: {}", e.getMessage());
        return Result.error(e.getMessage());
    }

    /**
     * 处理被@Valid注解的参数没有通过验证时产生的异常
     *
     * @param e 方法参数无效异常
     * @return 响应提示信息
     */
    @ExceptionHandler
    public Result<Object> exceptionHandler(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getFieldErrors();
        String logInfo = fieldErrors.stream()
                .map(fieldError -> "\n对象: " + fieldError.getObjectName()
                        + ", 字段: " + fieldError.getField()
                        + ", 拒绝值: " + fieldError.getRejectedValue()
                        + ", 提示信息: " + fieldError.getDefaultMessage()
                )
                .collect(Collectors.joining("; "));
        log.info("[log] 参数校验不通过 {}", logInfo);
        String message = fieldErrors.stream()
                .map((fieldError)-> fieldError.getField() + fieldError.getDefaultMessage())
                .collect(Collectors.joining("；"));
        return Result.error(message);
    }

    /**
     * @param e
     * @return 响应提示信息
     */
    @ExceptionHandler
    public Result<Object> exceptionHandler(ConstraintViolationException e) {
        log.error("[log] ConstraintViolationException: {}", e.toString());
        return Result.error(e.getMessage());
    }

}
