package com.lucky.service.base.sharding.bean;

import com.lucky.service.base.sharding.strategy.table.ShardingTableStrategy;

/**
 * 分表信息
 * @author <a href="mailto:jpzhong1994@gmail.com">zhongjinpeng</a>
 * @version 1.0
 * @date 2020/7/14 16:47
 *
 */
public class ShardingTableInfo {

    /**
     * 分表策略
     */
    private ShardingTableStrategy shardingTableStrategy;

    /**
     * 分表数量
     */
    private Integer shardingCount;

    public ShardingTableStrategy getShardingTableStrategy() {
        return shardingTableStrategy;
    }

    public void setShardingTableStrategy(ShardingTableStrategy shardingTableStrategy) {
        this.shardingTableStrategy = shardingTableStrategy;
    }

    public Integer getShardingCount() {
        return shardingCount;
    }

    public void setShardingCount(Integer shardingCount) {
        this.shardingCount = shardingCount;
    }
}
