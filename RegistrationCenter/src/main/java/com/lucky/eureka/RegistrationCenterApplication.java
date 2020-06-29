package com.lucky.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author zhongjinpeng
 * @version 1.0.0
 */
@EnableEurekaServer
@SpringBootApplication
public class RegistrationCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(RegistrationCenterApplication.class, args);
    }

}
