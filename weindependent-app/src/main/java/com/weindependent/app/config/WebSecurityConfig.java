package com.weindependent.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests().anyRequest()
//                .authenticated()
//                .and().formLogin()
//                .and().csrf();	// 开启 csrf 跨域请求保护
//    }

    public RequestMatcher csrfRequestMatcher = new RequestMatcher() {
        @Override
        public boolean matches(HttpServletRequest request) {
            // String url = request.getRequestURL().toString();
            // if (url.contains("/donate/") && !url.contains("/donate/receive-paypal-ipn")) {
            //     return true;  // 其他 /donate/ 路径启用CSRF
            // }
            return false; // IPN 路径不启用CSRF
        }
    };


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // 禁用 csrf，前后端分离模式不需要，因为是无状态的
        httpSecurity.csrf().disable()
                .authorizeRequests().anyRequest().permitAll()
                .and()
                // 禁用 Session存储 -- SessionCreationPolicy.STATELESS 表示永远不会创建会话
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable() // 禁用表单登录
//                .httpBasic().disable() // 禁用HTTP Basic认证
                .logout().disable()
                // 禁用网页嵌套
                .headers().frameOptions().disable();
        return httpSecurity.build();
    }
}
