package com.weindependent.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


/**
 * @author zlc
 * @date 2019-09-20
 */
@EnableOAuth2Sso
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${exit_url}")
    private String exit_url;

    @Autowired
    @Qualifier("resourceServerRequestMatcher")
    private RequestMatcher resources;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        RequestMatcher nonResoures = new NegatedRequestMatcher(resources);
        http.requestMatcher(nonResoures).authorizeRequests()
                .anyRequest().authenticated()
//        http.authorizeRequests()
////                .antMatchers("/oauth/**","/api/**", "/error", "/css/**", "/js/**", "/fonts/**",
////                        "/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**").permitAll()
//                .antMatchers("/", "/login")
//                .authenticated()
                .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl(exit_url)
                .and()
                .cors()
                .and()
                .csrf().disable();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        //允许带凭证
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        //对所有URL生效
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}

