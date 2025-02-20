package com.weindependent.app.config;

import com.weindependent.app.enums.ErrorCode;
import com.weindependent.app.exception.SignatureAuthException;
import org.springframework.core.MethodParameter;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

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
//        return returnValue instanceof List ? (ObjectUtils.isEmpty(returnValue) ? "[]" : getList(returnValue)) : (ObjectUtils.isEmpty(returnValue) ? "{}" : returnValue);
        return returnValue instanceof List ? (ObjectUtils.isEmpty(returnValue) ? "[]" : returnValue) : (ObjectUtils.isEmpty(returnValue) ? "{}" : returnValue);
    }

    private Map getList(Object returnValue) {
        Map map = new HashMap();
        map.put("data", returnValue);
        map.put("pageSize", 20);
        map.put("pageNo", 1);
        map.put("totalCount", 200);
        map.put("totalPage", 10);
        return map;
    }

}
