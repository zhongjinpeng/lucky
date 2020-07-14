package com.lucky.service.base.sharding.strategy;

import com.google.common.collect.Maps;
import com.lucky.service.base.annotation.Sharding;
import com.lucky.service.base.sharding.bean.ShardingTableInfo;
import com.lucky.service.base.sharding.strategy.table.ShardingTableStrategy;

import java.util.Map;

/**
 * @author <a href="mailto:jpzhong1994@gmail.com">zhongjinpeng</a>
 * @version 1.0
 * @date 2020/7/13 16:26
 *
 */
public abstract class AbstractShardingTableStrategy implements ShardingTableStrategy {

    protected static Map<String, ShardingTableInfo> shardingDataSourceInfoMap = Maps.newHashMap();

    public static void setShardingDataSourceInfoMap(Map<String, ShardingTableInfo> shardingDataSourceInfoMap) {
        AbstractShardingTableStrategy.shardingDataSourceInfoMap = shardingDataSourceInfoMap;
    }

    @Override
    public String getTargetTableName(Sharding sharding, String shardingKey) {
        Integer tableSuffix = calculateTableSuffix(sharding, shardingKey);
        return getTableName(sharding.tableName(), tableSuffix);
    }

    public String getTableName(String tableName, Integer shardingKey) {
        return tableName + UNDERLINE + shardingKey;
    }

}
