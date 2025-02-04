package com.weindependent.app.impl;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 此处应该从数据库或其他存储中获取用户信息
        // 这里演示一个简单的用户验证过程
        if ("user".equals(username)) {
            return User.withUsername(username)
                    .password("{bcrypt}$2a$10$uNVPZcCoeVbVsBv6ufr/ZuIbB2BtIT2frM1gE.6kKgdmLs0wS9Nk6")
                    .roles("USER")
                    .build();
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
}
