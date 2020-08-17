package com.lucky.service.organization;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author <a href="mailto:jpzhong1994@gmail.com">zhongjinpeng</a>
 * @version v1.0
 */
@SpringBootApplication
@MapperScan("com.lucky.service.organization.dao")
@ComponentScan(basePackages = "com.lucky.service")
public class LuckyOrganizationApplication {

    public static void main(String[] args) {
        SpringApplication.run(LuckyOrganizationApplication.class, args);
    }

}
