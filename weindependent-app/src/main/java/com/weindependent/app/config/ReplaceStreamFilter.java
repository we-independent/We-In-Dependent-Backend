package com.weindependent.app.config;


import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 解决request流只读取一次的问题
 * @desc 此类覆写不影响multipart/form-data的请求
 */
@Slf4j
public class ReplaceStreamFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
//        String contentType = httpRequest.getContentType();

//        // 判断是否为 multipart 请求
//        if (contentType != null && contentType.startsWith("multipart/form-data")) {
//            // 直接传递原始请求，不进行包装
//            chain.doFilter(request, response);
//        } else {
            // 非 multipart 请求时进行流包装
            ServletRequest requestWrapper = new RequestWrapper((HttpServletRequest) request);
            chain.doFilter(requestWrapper, response);
//        }
    }
}
