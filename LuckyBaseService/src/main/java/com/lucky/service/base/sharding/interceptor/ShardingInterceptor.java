package com.lucky.service.base.sharding.interceptor;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.util.JdbcConstants;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.lucky.service.base.annotation.Sharding;
import com.lucky.service.base.sharding.ShardingContext;
import com.lucky.service.base.sharding.strategy.table.DefaultShardingTableStrategy;
import com.lucky.service.base.sharding.strategy.table.ShardingTableStrategy;
import com.lucky.service.base.util.ResourceUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.sql.DataSource;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * mybatis插件对SQL进行拦截，需要在创建Statement之前进行拦截，可以使用该拦截器
 * @author <a href="mailto:jpzhong1994@gmail.com">zhongjinpeng</a>
 * @version 1.0
 * @date 2020/7/13 15:59
 */
@Intercepts({@Signature(method = "prepare", type = StatementHandler.class, args = {Connection.class, Integer.class})})
public class ShardingInterceptor implements Interceptor {

    @Autowired
    ApplicationContext applicationContext;

    private static final Logger logger = LoggerFactory.getLogger(ShardingInterceptor.class);

    private static final String DELEGATE_BOUND_SQL_SQL = "delegate.boundSql.sql";
    private static final String DELEGATE_MAPPED_STATEMENT_ID = "delegate.mappedStatement.id";
    private static final String DELEGATE_PARAMETER_HANDLER_PARAMETER_OBJECT = "delegate.parameterHandler.parameterObject";
    private static final String H_TARGET = "h.target";

    private static final String PARAM_1 = "param1";
    private static final String POINT = ".";

    private static final ShardingTableStrategy DEFAULT_SHARDING_TABLE_STRATEGY = new DefaultShardingTableStrategy();
    private static final Map<String, ShardingTableStrategy> SHARDING_STRATEGY_TABLE_MAP = Maps.newConcurrentMap();


    /**
     * 在拦截器中，获取当前sql对应的mybatis元信息，从元信息中获取对应mapper接口上的标记注解，用来获取分库分表信息，进行SQL的重写
     * @param invocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        // 获取target的元数据
        Object target = invocation.getTarget();
        logger.info("------target:{}", JSON.toJSONString(target));

        StatementHandler statementHandler = (StatementHandler) realTarget(invocation.getTarget());
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
        logger.info("------metaObject:{}", JSON.toJSONString(metaObject));

        // 获取类名
        String id =  (String)metaObject.getValue(DELEGATE_MAPPED_STATEMENT_ID);
        String className = id.substring(0, id.lastIndexOf("."));
        logger.info("------className:{}",className);

        // 获取Sharding注解
        Sharding sharding = Class.forName(className).getDeclaredAnnotation(Sharding.class);
        logger.info("------sharding:{}",sharding);

        if (sharding != null && sharding.sharding()) {
            logger.debug("------start sharding table------");

            String targetTableName = getTargetTableName(metaObject, sharding);
            logger.info("------target table name:{}",targetTableName);

            Map<String, String> tableNameMap = new ConcurrentHashMap<>(16);
            tableNameMap.put(sharding.tableName(),targetTableName);

            if(sharding.createTable()){
                logger.debug("------start auto create table------");
                String createTableSql = getShardingStrategy(sharding).getCreateTableSql();
                createTableSql = SQLUtils.refactor(createTableSql, JdbcConstants.MYSQL, tableNameMap);
                logger.info("------createTableSql:{}",createTableSql);
                createTable(createTableSql,targetTableName);
                logger.debug("------auto create table success------");
            }
            // 获取sql
            String sql = (String) metaObject.getValue(DELEGATE_BOUND_SQL_SQL);
            // 重写sql 用新sql代替旧sql, 完成所谓的sql
//            sql = sql.replace(sharding.tableName(), targetTableName);
            sql = SQLUtils.refactor(sql, JdbcConstants.MYSQL, tableNameMap);
            logger.info("------sql:{}",sql);
            metaObject.setValue(DELEGATE_BOUND_SQL_SQL, sql);

            logger.debug("------sharding table success------");
        }

        // 传递给下一个拦截器处理
        return invocation.proceed();
    }

    private void createTable(String sql,String targetTableName) throws Exception {
        /*
         * 获取数据库连接,判断目标表是否存在,不存在则需要创建表
         */
        DruidDataSource dataSource = applicationContext.getBean(DruidDataSource.class);
        Connection connection = dataSource.getConnection();
        logger.info("------dataSource connection info:{}",connection);
        Statement stat = connection.createStatement();
        ResultSet rs = connection.getMetaData().getTables(null, null, targetTableName, null);
        // 查看配置数据源信息
        if(!rs.next() ){
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
        }
        // 释放资源
        stat.close();
        connection.close();
    }

    private String getTargetTableName(MetaObject metaObject, Sharding sharding) throws Exception {
        String shardingKey = sharding.shardingKey();
        logger.info("------shardingKey:{}",shardingKey);
        Object parameterObject = metaObject.getValue(DELEGATE_PARAMETER_HANDLER_PARAMETER_OBJECT);
        logger.info("------parameterObject:{}",parameterObject);
        String firstLetter = shardingKey.substring(0, 1).toUpperCase();
        String getter = "get" + firstLetter + shardingKey.substring(1);
        logger.info("------getter:{}",getter);
        Method method = parameterObject.getClass().getMethod(getter);
        logger.info("------method:{}",method);
        Object value = method.invoke(parameterObject);
        logger.info("------shardingKey对应值:{}",value);
        String targetTableName = "";
        if (!StringUtils.isEmpty(shardingKey)) {
            targetTableName = getShardingStrategy(sharding).getTargetTableName(sharding, value);
        } else if (StringUtils.isEmpty(shardingKey) && !StringUtils.isEmpty(ShardingContext.getShardingTable())) {
            targetTableName = DEFAULT_SHARDING_TABLE_STRATEGY.getTargetTableName(sharding, ShardingContext.getShardingTable());
        }
        else {
            throw new RuntimeException("没有找到分表信息。shardingKey=" + shardingKey + "，ShardingContext=" + ShardingContext.getShardingTable());
        }
        return targetTableName;
    }

    private Object realTarget(Object target) {
        if (Proxy.isProxyClass(target.getClass())) {
            MetaObject metaObject = SystemMetaObject.forObject(target);
            return realTarget(metaObject.getValue(H_TARGET));
        }
        return target;
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
            logger.info("------parameterObject------:{}",parameterObject);
            shardingKey = (String) parameterObject;
        } else if (parameterObject instanceof Map) {
            Map<String, Object> parameterMap = (Map<String, Object>) parameterObject;
            Object param1 = parameterMap.get(PARAM_1);
            logger.info("------param1------:{}",param1);
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
