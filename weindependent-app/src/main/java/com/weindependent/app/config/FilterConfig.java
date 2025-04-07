package com.weindependent.app.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

/**
 * @desc 此类覆写不影响multipart/form-data的请求
 */
@Configuration
public class FilterConfig {
    /**
     * 注册过滤器
     *
     * @return FilterRegistrationBean
     */
    @Bean
    public FilterRegistrationBean someFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(replaceStreamFilter());
        registration.addUrlPatterns("/*");
        registration.setName("streamFilter");
        return registration;
    }
//    @Bean
//    public FilterRegistrationBean<ReplaceStreamFilter> someFilterRegistration() {
//        FilterRegistrationBean<ReplaceStreamFilter> registration = new FilterRegistrationBean<>();
//        registration.setFilter(new ReplaceStreamFilter());
//        registration.addUrlPatterns("/*");
//        registration.setName("streamFilter");
//
//        // 动态判断是否应用过滤器（对 multipart 请求不生效）
//        registration.addInitParameter("excludeContentType", "multipart/form-data");
//        registration.setOrder(1); // 确保优先级高于文件上传过滤器
//
//        return registration;
//    }

    /**
     * 实例化StreamFilter
     *
     * @return Filter
     */
    @Bean(name = "replaceStreamFilter")
    public Filter replaceStreamFilter() {
        return new ReplaceStreamFilter();
    }
}
