package com.anna_yanami.backend.config;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 返回结果集
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "统一结果集处理器") //
public class ResponseResult<T> {

    /**
     * 状态码
     */
    @Schema(description = "状态码") //
    private Integer code;

    /**
     * 状态信息
     */
    @Schema(description = "状态信息") //
    private Boolean status;

    /**
     * 返回信息
     */
    @Schema(description = "返回信息") //
    private String message;

    /**
     * 数据
     */
    @Schema(description = "数据") //
    private T data;

    private static <T> ResponseResult<T> response(Integer code, Boolean status, String message, T data) {
        ResponseResult<T> responseResult = new ResponseResult<>();
        responseResult.setCode(code);
        responseResult.setStatus(status);
        responseResult.setMessage(message);
        responseResult.setData(data);
        return responseResult;
    }

    private static <T> ResponseResult<T> response(Integer code, Boolean status, String message) {
        ResponseResult<T> responseResult = new ResponseResult<>();
        responseResult.setCode(code);
        responseResult.setStatus(status);
        responseResult.setMessage(message);
        return responseResult;
    }

    public static <T> ResponseResult<T> success() {
        return response(HttpStatusEnum.SUCCESS.getCode(), true, HttpStatusEnum.SUCCESS.getMessage(), null);
    }

    public static <T> ResponseResult<T> success(HttpStatusEnum httpResponseEnum) {
        return response(httpResponseEnum.getCode(), true, httpResponseEnum.getMessage());
    }

    public static <T> ResponseResult<T> success(Integer code, String message) {
        return response(code, true, message);
    }

    public static <T> ResponseResult<T> success(String message, T data) {
        return response(HttpStatusEnum.SUCCESS.getCode(), true, message, data);
    }

    public static <T> ResponseResult<T> success(Integer code, String message, T data) {
        return response(code, true, message, data);
    }

    public static <T> ResponseResult<T> success(T data) {
        return response(HttpStatusEnum.SUCCESS.getCode(), true, HttpStatusEnum.SUCCESS.getMessage(), data);
    }

    public static <T> ResponseResult<T> success(String message) {
        return response(HttpStatusEnum.SUCCESS.getCode(), true, message, null);
    }

    public static <T> ResponseResult<T> fail() {
        return response(HttpStatusEnum.ERROR.getCode(), false, HttpStatusEnum.ERROR.getMessage(), null);
    }

    public static <T> ResponseResult<T> fail(HttpStatusEnum httpResponseEnum) {
        return response(httpResponseEnum.getCode(), false, httpResponseEnum.getMessage());
    }

    public static <T> ResponseResult<T> fail(Integer code, String message) {
        return response(code, false, message);
    }

    public static <T> ResponseResult<T> fail(String message, T data) {
        return response(HttpStatusEnum.ERROR.getCode(), false, message, data);
    }

    public static <T> ResponseResult<T> fail(Integer code, String message, T data) {
        return response(code, false, message, data);
    }

    public static <T> ResponseResult<T> fail(T data) {
        return response(HttpStatusEnum.ERROR.getCode(), false, HttpStatusEnum.ERROR.getMessage(), data);
    }

    public static <T> ResponseResult<T> fail(String message) {
        return response(HttpStatusEnum.ERROR.getCode(), false, message, null);
    }
}
