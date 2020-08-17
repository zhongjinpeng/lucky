package com.lucky.service.base.sharding.strategy.table;


import com.lucky.service.base.annotation.Sharding;

/**
 * 分表策略:对SQL进行重写时，需要根据一定的策略进行计算数据所在分表，定义如下分表策略接口
 * @author <a href="mailto:jpzhong1994@gmail.com">zhongjinpeng</a>
 * @version 1.0
 * @date 2020/7/13 16:10
 *
 */
public interface ShardingTableStrategy {

    String UNDERLINE = "_";

    /**
     * 获取分表位的实际表名
     *
     * @param sharding Sharding信息
     * @param shardingKey 分库分表因子
     * @return 带分表位的实际表名
     */
    String getTargetTableName(Sharding sharding, Object shardingKey);

    /**
     * 获取自动建表的sql
     *
     * @return sql
     */
    String getCreateTableSql();

    /**
     * 计算分表
     *
     * @param sharding    Sharding信息
     * @param shardingKey 分库分表因子
     * @return 计算分表
     */
    Integer calculateTableSuffix(Sharding sharding, Object shardingKey);
}
