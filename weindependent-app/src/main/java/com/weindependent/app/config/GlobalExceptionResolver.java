package com.weindependent.app.config;

import com.weindependent.app.aspect.SignatureAuthAspect;
import com.weindependent.app.enums.ErrorCode;
import com.weindependent.app.exception.ResponseException;
import com.weindependent.app.exception.SignatureAuthException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionResolver{

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("code", ErrorCode.INVALID_PARAM.getCode());
        error.put("msg", ex.getBindingResult().getFieldError().getDefaultMessage());
        error.put("data", "");
        return error;
    }

    @ExceptionHandler(SignatureAuthException.class)
    public Map<String, Object> handleSignatureException(SignatureAuthException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("code", ex.getCode());
        error.put("msg", ex.getMessage());
        error.put("data", "");
        return error;
    }

    @ExceptionHandler(Exception.class)
    public Map<String, Object> handleGenericException(Exception e) {
        Map<String, Object> error = new HashMap<>();
        error.put("code", ErrorCode.UNDEFINED_ERROR.getCode());
        error.put("msg", StringUtils.isEmpty(e.getMessage()) ? "系统异常，请稍后操作" : e.getMessage());
        error.put("data", "");
        return error;
    }
}
