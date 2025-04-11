package com.weindependent.app.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.email", ignoreUnknownFields = false)
public class EmailConfig {
    private String host;
    private int port;
    private String username;
    private String password;
    private String senderName;
    private String protocol;
    private String encoding;

    @Override
    public String toString() {
        return "EmailConfig{" +
                "host='" + host + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", senderName='" + senderName + '\'' +
                ", protocol='" + protocol + '\'' +
                ", encoding='" + encoding + '\'' +
                '}';
    }
}
