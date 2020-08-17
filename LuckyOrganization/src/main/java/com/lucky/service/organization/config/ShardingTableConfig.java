package com.lucky.service.organization.config;

import com.lucky.service.base.sharding.interceptor.ShardingInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="mailto:jpzhong1994@gmail.com">zhongjinpeng</a>
 * @version 1.0
 * @date 2020/7/14 17:36
 *
 */
@Configuration
public class ShardingTableConfig {
    @Bean
    public ShardingInterceptor shardingInterceptor() {
        return new ShardingInterceptor();
    }
}
