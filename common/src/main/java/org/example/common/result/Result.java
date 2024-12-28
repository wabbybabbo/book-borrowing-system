package org.example.common.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 响应结果封装类
 * @param <T> 响应数据
 */
@Data
@Schema(description = "响应结果封装类")
public class Result<T> implements Serializable {

    @Schema(description = "响应码 1:成功 0:失败")
    private Integer code;

    @Schema(description = "提示信息")
    private String msg;

    @Schema(description = "响应数据")
    private T data;

    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.code = 1;
        return result;
    }

    public static <T> Result<T> success(String msg) {
        Result<T> result = new Result<>();
        result.code = 1;
        result.msg = msg;
        return result;
    }

    public static <T> Result<T> success(T object) {
        Result<T> result = new Result<>();
        result.code = 1;
        result.data = object;
        return result;
    }

    public static <T> Result<T> success(String msg, T object) {
        Result<T> result = new Result<>();
        result.code = 1;
        result.msg = msg;
        result.data = object;
        return result;
    }

    public static <T> Result<T> error(String msg) {
        Result<T> result = new Result<>();
        result.code = 0;
        result.msg = msg;
        return result;
    }

}
