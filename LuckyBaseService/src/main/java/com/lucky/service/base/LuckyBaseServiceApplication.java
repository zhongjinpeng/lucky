package com.lucky.service.base;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @SpringBootApplication 是一个复合注解，包括@ComponentScan，和@SpringBootConfiguration，@EnableAutoConfiguration
 * @SpringBootConfiguration 继承自@Configuration，二者功能也一致，标注当前类是配置类，并会将当前类内声明的一个或多个以@Bean注解标记的方法的实例纳入到srping容器中，并且实例名就是方法名
 * @EnableAutoConfiguration 的作用启动自动的配置，@EnableAutoConfiguration注解的意思就是Springboot根据你添加的jar包来配置你项目的默认配置，比如根据spring-boot-starter-web ，来判断你的项目是否需要添加了webmvc和tomcat，就会自动的帮你配置web项目中所需要的默认配置
 * @ComponentScan 的功能其实就是自动扫描并加载符合条件的组件或bean定义，最终将这些bean定义加载到容器中。我们可以通过basePackages等属性指定@ComponentScan自动扫描的范围，如果不指定，则默认Spring框架实现从声明@ComponentScan所在类的package进行扫描，默认情况下是不指定的，所以SpringBoot的启动类最好放在root package下
 *
 * @author: <a href="mailto:jpzhong1994@gmail.com">zhongjinpeng</a>
 * @version: v1.0
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.lucky.service")
public class LuckyBaseServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LuckyBaseServiceApplication.class, args);
    }

}
