package com.weindependent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

/**
 * main
 * @author zetor
 */
@SpringBootApplication
@EntityScan("com.weindependent.entity")
public class Oauth2ServerApp {

    public static void main(String[] args) {
        SpringApplication.run(Oauth2ServerApp.class, args);
    }

}
