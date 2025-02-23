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
            // Enable the CSRF
            if (request.getRequestURL().indexOf("/donate/") != -1) {
                return true;
            }
            return false;
        }

    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // 禁用 csrf，前后端分离模式不需要，因为是无状态的
        httpSecurity.csrf().csrfTokenRepository(new HttpSessionCsrfTokenRepository())
                .requireCsrfProtectionMatcher(csrfRequestMatcher)
//        httpSecurity.csrf().disable()
                .and()
                // 配置允许请求
//                .authorizeRequests(expressionInterceptUrlRegistry ->
//                        expressionInterceptUrlRegistry.antMatchers("/login", "/captchaImage", "/user/list").permitAll()
//                                // 下面是追加对资源的释放，并设置get请求
//                                .antMatchers(HttpMethod.GET, "/", "222.html").permitAll()
//                                // 其他请求需要认证
////                                .anyRequest().authenticated())
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
