package com.lucky.service.base.sharding.interceptor;

import com.google.common.collect.Maps;
import com.lucky.service.base.annotation.Sharding;
import com.lucky.service.base.sharding.ShardingContext;
import com.lucky.service.base.sharding.strategy.table.DefaultShardingTableStrategy;
import com.lucky.service.base.sharding.strategy.table.ShardingTableStrategy;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Connection;
import java.util.Map;
import java.util.Properties;

/**
 * mybatis插件对SQL进行拦截，需要在创建Statement之前进行拦截，可以使用该拦截器
 * @author <a href="mailto:jpzhong1994@gmail.com">zhongjinpeng</a>
 * @version 1.0
 * @date 2020/7/13 15:59
 */
@Intercepts({@Signature(method = "prepare", type = StatementHandler.class, args = {Connection.class, Integer.class})})
public class ShardingInterceptor implements Interceptor {

    private static final Logger logger = LoggerFactory.getLogger(ShardingInterceptor.class);

    private static final String DELEGATE_BOUND_SQL_SQL = "delegate.boundSql.sql";
    private static final String DELEGATE_MAPPED_STATEMENT_ID = "delegate.mappedStatement.id";
    private static final String DELEGATE_PARAMETER_HANDLER_PARAMETER_OBJECT = "delegate.parameterHandler.parameterObject";
    private static final String PARAM_1 = "param1";
    private static final String POINT = ".";

    private static final ShardingTableStrategy DEFAULT_SHARDING_TABLE_STRATEGY = new DefaultShardingTableStrategy();
    private static final Map<String, ShardingTableStrategy> SHARDING_STRATEGY_TABLE_MAP = Maps.newConcurrentMap();

    @Autowired
    private ShardingTableStrategy shardingTableStrategy;

    /**
     * 在拦截器中，获取当前sql对应的mybatis元信息，从元信息中获取对应mapper接口上的标记注解，用来获取分库分表信息，进行SQL的重写
     * @param invocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object target = invocation.getTarget();
        logger.info("------当前拦截对象------:{}",target);

        // 获取target的元数据
        StatementHandler statementHandler = (StatementHandler)(target);
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
        logger.info("------target的元数据------:{}",metaObject);

        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue(DELEGATE_MAPPED_STATEMENT_ID);
        String id = mappedStatement.getId();
        String className = id.substring(0, id.lastIndexOf("."));

        // 获取Sharding注解
        Sharding sharding = Class.forName(className).getDeclaredAnnotation(Sharding.class);

        if (sharding != null && sharding.sharding()) {
            String targetTableName = getTargetTableName(metaObject, sharding);
            logger.info("------目标表名------:{}",targetTableName);

            if(sharding.createTable()){
                logger.debug("------开始自动建表------");
                logger.debug("------自动建表成功------");
            }
            // 获取sql
            String sql = (String) metaObject.getValue(DELEGATE_BOUND_SQL_SQL);
            // 重写sql 用新sql代替旧sql, 完成所谓的sql
            sql = sql.replaceAll(sharding.tableName(), targetTableName);
            metaObject.setValue(DELEGATE_BOUND_SQL_SQL, sql);
        }

        // 传递给下一个拦截器处理
        return invocation.proceed();
    }

    private String getTargetTableName(MetaObject metaObject, Sharding sharding) throws Exception {
        String shardingKey = getShardingKey(metaObject);
        String targetTableName;
        if (!StringUtils.isEmpty(shardingKey)) {
            targetTableName = getShardingStrategy(sharding).getTargetTableName(sharding, shardingKey);
        } else if (StringUtils.isEmpty(shardingKey) && !StringUtils.isEmpty(ShardingContext.getShardingTable())) {
            targetTableName = DEFAULT_SHARDING_TABLE_STRATEGY.getTargetTableName(sharding, ShardingContext.getShardingTable());
        }
        else {
            throw new RuntimeException("没有找到分表信息。shardingKey=" + shardingKey + "，ShardingContext=" + ShardingContext.getShardingTable());
        }
        return targetTableName;
    }

    private ShardingTableStrategy getShardingStrategy(Sharding sharding) throws Exception {
        String strategyClassName = sharding.strategy();
        ShardingTableStrategy shardingTableStrategy = SHARDING_STRATEGY_TABLE_MAP.get(strategyClassName);
        if (shardingTableStrategy == null) {
            ShardingTableStrategy strategy = (ShardingTableStrategy) Class.forName(strategyClassName).newInstance();
            SHARDING_STRATEGY_TABLE_MAP.putIfAbsent(strategyClassName, strategy);
            shardingTableStrategy = SHARDING_STRATEGY_TABLE_MAP.get(strategyClassName);
        }
        return shardingTableStrategy;
    }

    /**
     * 默认取第一个参数作为分表键
     * @param metaObject
     * @return
     */
    private String getShardingKey(MetaObject metaObject) {
        String shardingKey = null;
        Object parameterObject = metaObject.getValue(DELEGATE_PARAMETER_HANDLER_PARAMETER_OBJECT);
        if (parameterObject instanceof String) {
            shardingKey = (String) parameterObject;
        } else if (parameterObject instanceof Map) {
            Map<String, Object> parameterMap = (Map<String, Object>) parameterObject;
            Object param1 = parameterMap.get(PARAM_1);
            if (param1 instanceof String) {
                shardingKey = (String) param1;
            }
        }
        return shardingKey;
    }

    @Override
    public Object plugin(Object target) {
        // 当目标类是StatementHandler类型时，才包装目标类，否者直接返回目标本身, 减少目标被代理的次数
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
