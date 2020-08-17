package com.lucky.service.organization.config;

import com.lucky.service.base.annotation.Sharding;
import com.lucky.service.base.sharding.strategy.ShardingTableStrategy;

/**
 * @author <a href="mailto:jpzhong1994@gmail.com">zhongjinpeng</a>
 * @version 1.0
 * @date 2020/7/14 16:20
 *
 */
public class UserSharingTableStrategy implements ShardingTableStrategy {
    @Override
    public String getTargetTableName(Sharding sharding, String shardingKey) {
        String originalTableName = sharding.tableName();
        return null;
    }

    @Override
    public Integer calculateTableSuffix(Sharding sharding, String shardingKey) {
        return null;
    }
}
