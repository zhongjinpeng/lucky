package com.lucky.service.base.sharding.strategy.table;

import com.lucky.service.base.annotation.Sharding;

/**
 * 使用分表因子shardingKey直接作为分表位
 * @author <a href="mailto:jpzhong1994@gmail.com">zhongjinpeng</a>
 * @version 1.0
 * @date 2020/7/14 17:04
 *
 */
public class DefaultShardingTableStrategy extends AbstractShardingTableStrategy{

    @Override
    public Integer calculateTableSuffix(Sharding sharding, String shardingKey) {
        return Integer.valueOf(shardingKey);
    }
}
