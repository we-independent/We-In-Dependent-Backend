package com.weindependent.app.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class ReturnValueConfig implements InitializingBean {
    @Autowired
    RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    @Override
    public void afterPropertiesSet() throws Exception {
        List<HandlerMethodReturnValueHandler> originHandlers = requestMappingHandlerAdapter.getReturnValueHandlers();
        List<HandlerMethodReturnValueHandler> newHandlers = new ArrayList<>(originHandlers.size());
        for (HandlerMethodReturnValueHandler originHandler : originHandlers) {
            if (originHandler instanceof RequestResponseBodyMethodProcessor) {
                newHandlers.add(new ExtHandlerMethodReturnValueHandler(originHandler));
            }else{
                newHandlers.add(originHandler);
            }
        }
        requestMappingHandlerAdapter.setReturnValueHandlers(newHandlers);
    }
}