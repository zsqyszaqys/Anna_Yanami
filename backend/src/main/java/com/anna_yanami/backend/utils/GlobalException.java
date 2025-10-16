package com.anna_yanami.backend.utils;

import com.anna_yanami.backend.config.ResponseResult;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * 全局异常处理器（带 @ResponseStatus，返回正确的 HTTP 状态码）
 */
@RestControllerAdvice
public class GlobalException {

    private static final Logger log = LoggerFactory.getLogger(GlobalException.class);

    // 403：权限不足
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseResult<Object> handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}', 权限校验失败: {}", requestURI, e.getMessage());
        return ResponseResult.fail(403, "没有权限访问该资源");
    }

    // 405：方法不被允许
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ResponseResult<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e,
                                                                      HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}', 不支持'{}'请求方法", requestURI, e.getMethod());
        return ResponseResult.fail(405, "请求方法不支持");
    }

    // 404：静态资源/路径未找到（例如访问了 /me 但未配置资源）
    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseResult<Object> handleNoResourceFound(NoResourceFoundException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}', 资源不存在: {}", requestURI, e.getMessage());
        return ResponseResult.fail(404, "资源不存在：" + requestURI);
    }

    // 400：请求体解析失败（JSON 语法错误、类型不匹配等）
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseResult<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String msg = e.getMostSpecificCause() != null ? e.getMostSpecificCause().getMessage() : e.getMessage();
        log.error("请求地址'{}', JSON 解析失败: {}", requestURI, msg);
        return ResponseResult.fail(400, "请求体格式错误：" + msg);
    }

    // 400：业务异常（受控）
    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseResult<Object> handleServiceException(ServiceException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.warn("业务异常 - 请求地址: {}, 异常信息: {}", requestURI, e.getMessage());
        Integer code = e.getCode();
        return ResponseResult.fail(code != null ? code : 400, e.getMessage());
    }

    // 400：参数绑定异常（非 RequestBody）
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseResult<Object> handleBindException(BindException e) {
        log.error(e.getMessage(), e);
        String message = (e.getAllErrors().isEmpty() ? "参数校验失败" : e.getAllErrors().get(0).getDefaultMessage());
        return ResponseResult.fail(400, message);
    }

    // 400：RequestBody 校验失败（@Valid / @Validated）
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseResult<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        String message = (e.getBindingResult().getFieldError() != null)
                ? e.getBindingResult().getFieldError().getDefaultMessage()
                : "参数校验失败";
        return ResponseResult.fail(400, message);
    }

    // 500：未知运行时异常
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseResult<Object> handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}', 发生未知运行时异常", requestURI, e);
        return ResponseResult.fail(500, "系统运行时异常");
    }

    // 500：系统异常（兜底）
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseResult<Object> handleException(Exception e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}', 发生系统异常", requestURI, e);
        return ResponseResult.fail(500, "系统异常，请稍后重试");
    }
}