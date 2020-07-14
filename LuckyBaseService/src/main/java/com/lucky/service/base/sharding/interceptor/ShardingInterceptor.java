package com.lucky.service.base.interceptor;

import com.lucky.service.base.annotation.Sharding;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object target = invocation.getTarget();

        StatementHandler statementHandler = (StatementHandler)(target);
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);

        String id = (String) metaObject.getValue(DELEGATE_MAPPED_STATEMENT_ID);
        String className = id.substring(0, id.lastIndexOf("."));
        Sharding sharding = Class.forName(className).getDeclaredAnnotation(Sharding.class);
        if (sharding != null && sharding.sharding()) {
            String sql = (String) metaObject.getValue(DELEGATE_BOUND_SQL_SQL);
            sql = sql.replaceAll(sharding.tableName(), getTargetTableName(metaObject, sharding));
            metaObject.setValue(DELEGATE_BOUND_SQL_SQL, sql);
        }
        return invocation.proceed();
    }

    private String getTargetTableName(MetaObject metaObject, Sharding sharding) throws Exception {
        String shardingKey = getShardingKey(metaObject);
        String targetTableName;
        if (!StringUtils.isEmpty(shardingKey)) {
            targetTableName = getShardingStrategy(sharding).getTargetTableName(sharding, shardingKey);
        } else if (StringUtils.isEmpty(shardingKey) && !StringUtils.isEmpty(ShardingContext.getShardingTable())) {
            targetTableName = DEFAULT_SHARDING_STRATEGY.getTargetTableName(sharding, ShardingContext.getShardingTable());
        } else {
            throw new RuntimeException("没有找到分表信息。shardingKey=" + shardingKey + "，ShardingContext=" + ShardingContext.getShardingTable());
        }
        return targetTableName;
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
        return null;
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
