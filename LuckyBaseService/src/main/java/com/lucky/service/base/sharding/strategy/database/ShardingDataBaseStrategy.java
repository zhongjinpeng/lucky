package com.lucky.service.base.sharding.strategy;

/**
 * 分库策略:
 * @author <a href="mailto:jpzhong1994@gmail.com">zhongjinpeng</a>
 * @version 1.0
 * @date 2020/7/14 14:18
 *
 */
public interface ShardingDataBaseStrategy {

    /**
     * 计算获取对应分库序号
     *
     * @param sharingDataBaseCount    分库数量
     * @param sharingTableCount       分表数量
     * @param currentShardingTableKey 当前分表位
     * @return 分库序号
     */
    Integer calculate(int sharingDataBaseCount, int sharingTableCount, int currentShardingTableKey);
}
