package com.lucky.service.base.sharding.strategy.table;

import com.lucky.service.base.annotation.Sharding;
import com.lucky.service.base.sharding.interceptor.ShardingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 使用分表因子shardingKey直接作为分表位
 * @author <a href="mailto:jpzhong1994@gmail.com">zhongjinpeng</a>
 * @version 1.0
 * @date 2020/7/14 17:04
 *
 */
public class DefaultShardingTableStrategy extends AbstractShardingTableStrategy{

    private static final Logger logger = LoggerFactory.getLogger(DefaultShardingTableStrategy.class);

    @Override
    public String getCreateTableSql() {
        return null;
    }

    @Override
    public Integer calculateTableSuffix(Sharding sharding, Object shardingKey) {
        logger.info("------分表因子------:{}",shardingKey);
        if(shardingKey instanceof Integer){
            return (Integer) shardingKey;
        }
        throw new RuntimeException("分表因子异常。");
    }

}
