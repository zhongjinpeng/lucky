package com.lucky.service.base.sharding;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author <a href="mailto:jpzhong1994@gmail.com">zhongjinpeng</a>
 * @version 1.0
 * @date 2020/7/14 14:16
 * @description TODO
 */
public class ShardingDataSource extends AbstractRoutingDataSource {

    /**
     * ShardingContext.getShardingDatabase() 为库名+分库序号
     *
     * @return
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return ShardingContext.getShardingDatabase();
    }
}
