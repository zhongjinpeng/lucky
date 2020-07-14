package com.lucky.service.base.annotation;


import com.lucky.service.base.sharding.strategy.table.ShardingTableStrategy;

/**
 * 通过注解进行元数据的标记哪些表的SQL需要被拦截重写,将该注解标记在mybatis的mapper接口上，便可以在拦截器中获取分库分表规则
 * @author <a href="mailto:jpzhong1994@gmail.com">zhongjinpeng</a>
 * @version 1.0
 * @date 2020/7/13 16:03
 *
 */
public @interface Sharding {

    /**
     * 是否分表
     * @return true or false
     */
    boolean sharding() default true;

    /**
     * 是否自动建表
     * @return true or false
     */
    boolean createTable() default true;

    /**
     * 库名
     * @return 数据库名称
     */
    String databaseName();

    /**
     * 表名
     * @return 数据库表名
     */
    String tableName();

    /**
     * 分表策略
     * @see ShardingTableStrategy
     * @return 分表策略
     */
    String strategy();

    /**
     * 分表数量
     * @return 分表数量
     */
    int count() default 0;


}
