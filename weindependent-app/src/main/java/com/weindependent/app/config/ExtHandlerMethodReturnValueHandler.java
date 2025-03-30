package com.weindependent.app.config;

import com.weindependent.app.enums.ErrorCode;
import com.weindependent.app.exception.SignatureAuthException;
import org.springframework.core.MethodParameter;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExtHandlerMethodReturnValueHandler implements HandlerMethodReturnValueHandler {
    private HandlerMethodReturnValueHandler handler;

    public ExtHandlerMethodReturnValueHandler(HandlerMethodReturnValueHandler handler) {
        this.handler = handler;
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return handler.supportsReturnType(returnType);
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        // 排除 Swagger 相关请求
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        if (attrs instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes) attrs).getRequest();
            String uri = request.getRequestURI();
            if (uri.startsWith("/swagger-ui") || uri.startsWith("/v3/api-docs") || uri.startsWith("/webjars/swagger-ui")) {
                handler.handleReturnValue(returnValue, returnType, mavContainer, webRequest); // Swagger 请求不处理
                return;
            }
        }
        // 非 Swagger 请求继续使用原有逻辑判断
        Map<String, Object> map = new HashMap<>();
        try {
            map.put("code", ErrorCode.SUCCESS.getCode());
            map.put("msg", ErrorCode.SUCCESS.getTitle());
            map.put("data", getValue(returnValue));
            handler.handleReturnValue(map, returnType, mavContainer, webRequest);
        } catch (SignatureAuthException signatureAuthException) {
            map.put("code", signatureAuthException.getCode());
            map.put("msg", signatureAuthException.getMessage());
            map.put("data", getValue(returnValue));
            handler.handleReturnValue(map, returnType, mavContainer, webRequest);
        }
    }

    private Object getValue(Object returnValue) {
        // 排除 Swagger 相关请求
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        if (attrs instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes) attrs).getRequest();
            String uri = request.getRequestURI();
            if (uri.startsWith("/swagger-ui") || uri.startsWith("/v3/api-docs") || uri.startsWith("/webjars/swagger-ui")) {
                return returnValue;
            }
        }
        // 非 Swagger 请求继续使用原有逻辑判断
//        return returnValue instanceof List ? (ObjectUtils.isEmpty(returnValue) ? "[]" : getList(returnValue)) : (ObjectUtils.isEmpty(returnValue) ? "{}" : returnValue);
        return returnValue instanceof List ? (ObjectUtils.isEmpty(returnValue) ? "[]" : returnValue) : (ObjectUtils.isEmpty(returnValue) ? "{}" : returnValue);
    }

}
