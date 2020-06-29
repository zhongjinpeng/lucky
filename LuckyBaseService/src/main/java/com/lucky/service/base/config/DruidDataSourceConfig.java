package com.lucky.service.base.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @ConditionalOnClass 是由元注解@Conditional(OnClassCondition.class定义的注解，我们知道，@Conditional是条件注解，只有条件为真时，@Conditional注解的类、方法才会被加载到Spring组件容器中。对于上面的实例代码段，只有当MBeanExporter.class已经包含在classpath中（具体校验类似于Class.forName的加载逻辑，当目标类包含在classpath中，方法返回为true，否则返回false），OnClassCondition#matches()才会返回为true。
 * @ConditionalOnProperty 与@ConditionalOnClass类似，@ConditionalOnProperty是另一个@Conditional类型变量，是由元注解@Conditional(OnPropertyCondition.class)**所定义的注解。
 * 上述两个注解都为条件注解
 * Spring Boot 2.x 使用HikariDataSource作为默认连接池
 * HikariDataSource和DruidDataSource同时implements DataSource 此时若要使用DruidDataSource作为连接池,需要通过@Primary指定DruidDataSource为DataSource的默认注入
 *
 * @author: <a href="mailto:jpzhong1994@gmail.com">zhongjinpeng</a>
 * @version: v1.0
 */
@Slf4j
@Data
@Configuration
@ConditionalOnClass(DruidDataSource.class)
@ImportResource(locations={"classpath:spring/config-druid.xml"})
@ConditionalOnProperty(name = "spring.datasource.type", havingValue = "com.alibaba.druid.pool.DruidDataSource", matchIfMissing = true)
public class DruidDataSourceConfig {

//    private String driverClassName;
//
//    private String url;
//
//    private String username;
//
//    private String password;
//
//    private int initialSize;
//
//    private int minIdle;
//
//    private int maxActive;
//
//    private int maxWait;
//
//    private boolean poolPreparedStatements;
//
//    private int maxPoolPreparedStatementPerConnectionSize;
//
//    private int timeBetweenEvictionRunsMillis;
//
//    private int minEvictableIdleTimeMillis;
//
//    private boolean testWhileIdle;
//
//    private boolean testOnBorrow;
//
//    private boolean testOnReturn;
//
//    private String validationQuery;
//
//    private String filters;
//
//
//    /**
//     *  Spring Boot 2.x 使用HikariDataSource作为默认连接池
//     *  HikariDataSource和DruidDataSource同时implements DataSource 此时若要使用DruidDataSource作为连接池,需要通过@Primary指定DruidDataSource为DataSource的默认注入
//     * @return
//     */
//    @Bean
//    @Primary
//    @ConfigurationProperties(prefix = "spring.datasource")

//    public DataSource dataSource(){
//        DruidDataSource dataSource = new DruidDataSource();

//        dataSource.setDriverClassName(driverClassName);
//        dataSource.setUrl(url);
//        dataSource.setUsername(username);
//        dataSource.setPassword(password);
//
//        dataSource.setInitialSize(initialSize);
//        dataSource.setMinIdle(minIdle);
//        dataSource.setMaxActive(maxActive);
//        dataSource.setMaxWait(maxWait);
//
//        dataSource.setPoolPreparedStatements(poolPreparedStatements);
//        dataSource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
//        dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
//        dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
//
//        dataSource.setValidationQuery(validationQuery);
//        dataSource.setTestWhileIdle(testWhileIdle);
//        dataSource.setTestOnBorrow(testOnBorrow);
//        dataSource.setTestOnReturn(testOnReturn);
//
//        log.info("------init DataSource,DataSource Class Name:{}",dataSource.getClass().getName());
//        try {
//             log.info("------init DataSource,connection:{}",dataSource.getConnection());
//        }catch (Exception e){
//            log.error(e.getMessage());
//        }
//
//        return dataSource;
//    }

    /**
     * 配置Druid监控的StatViewServlet
     * @return
     */
    @Bean
    public ServletRegistrationBean<StatViewServlet> druidServlet() {
        ServletRegistrationBean<StatViewServlet> servletRegistrationBean = new ServletRegistrationBean<>();
        servletRegistrationBean.setServlet(new StatViewServlet());
        servletRegistrationBean.addUrlMappings("/druid/*");
        Map<String, String> initParameters = new HashMap<String, String>();
        initParameters.put("loginUsername", "admin");
        initParameters.put("loginPassword", "admin");
        initParameters.put("resetEnable", "false");
        initParameters.put("allow", "");
        //initParameters.put("deny", "192.168.20.38");// IP黑名单 (存在共同时，deny优先于allow)
        servletRegistrationBean.setInitParameters(initParameters);
        return servletRegistrationBean;
    }

    /**
     * 配置Druid监控的WebStatFilter
     * @return
     */
    @Bean
    public FilterRegistrationBean<WebStatFilter> filterRegistrationBean() {
        FilterRegistrationBean<WebStatFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }

}
