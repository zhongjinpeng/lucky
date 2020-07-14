package com.lucky.service.base.sharding.strategy.database;

import com.lucky.service.base.sharding.strategy.database.ShardingDataBaseStrategy;

/**
 * 默认分库策略
 * @author <a href="mailto:jpzhong1994@gmail.com">zhongjinpeng</a>
 * @version 1.0
 * @date 2020/7/14 14:19
 * @description TODO
 */
public class DefaultShardingDataBaseStrategy implements ShardingDataBaseStrategy {

    @Override
    public Integer calculate(int sharingDataBaseCount, int sharingTableCount, int currentShardingTableKey) {
        if (sharingTableCount >= sharingDataBaseCount && sharingTableCount % sharingDataBaseCount == 0) {
            int base = sharingTableCount / sharingDataBaseCount;
            return currentShardingTableKey / base;
        }
        throw new RuntimeException("分库分表规则配置错误");
    }
}
