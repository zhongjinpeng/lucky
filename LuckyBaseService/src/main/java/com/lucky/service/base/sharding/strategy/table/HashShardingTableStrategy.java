package com.lucky.service.base.sharding.strategy.table;


import com.lucky.service.base.annotation.Sharding;

/**
 * 对分表因子shardingKey进行hash取模计算得到分表位
 * @author <a href="mailto:jpzhong1994@gmail.com">zhongjinpeng</a>
 * @version 1.0
 * @date 2020/7/13 16:28
 *
 */
public class HashShardingTableStrategy extends AbstractShardingTableStrategy {

    @Override
    public Integer calculateTableSuffix(Sharding sharding, String shardingKey) {
        return Math.abs(shardingKey.hashCode()) % sharding.count();
    }
}
