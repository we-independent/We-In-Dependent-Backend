package com.weindependent.app.config;

import com.weindependent.app.aspect.SignatureAuthAspect;
import com.weindependent.app.enums.ErrorCode;
import com.weindependent.app.exception.SignatureAuthException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 统一异常处理，不论是正常跳转请求
 * @desc ajax请求暂不支持
 */
@Slf4j
@Component
@RestControllerAdvice
public class GlobalExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
        Map<String, Object> map = new HashMap<>();
        if (handler instanceof HandlerMethod) {
            Map<String, String> headerMap = SignatureAuthAspect.getSignHeader(request);
            String requestUrl = request.getRequestURL().toString();
            if (e instanceof SignatureAuthException) {
                SignatureAuthException exception = (SignatureAuthException) e;
                log.error("签名验证异常,当前请求URL：{}，当前请求Header：{}，异常信息：{}", requestUrl, headerMap, exception);
                map.put("code", exception.getCode());
                map.put("msg", exception.getMessage());
                map.put("data", "");
            } else {
                String body = null;
                try {
                    body = new RequestWrapper(request).getBodyString();
                } catch (Exception exception) {
                    log.error("当前请求URL：{}，RequestWrapper invoke getBodyString occur exception: ", requestUrl, exception);
                }
                log.error("系统异常,当前请求URL：{}，当前请求Header：{}，当前请求Body：{}，异常信息：{}", requestUrl, headerMap, body, e);
                map.put("code", ErrorCode.UNDEFINED_ERROR.getCode());
                map.put("msg", StringUtils.isEmpty(e.getMessage()) ? "系统异常，请稍后操作":e.getMessage());
                map.put("data", "");
            }
        }
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
        modelAndView.addAllObjects(map);
        return modelAndView;
    }
}