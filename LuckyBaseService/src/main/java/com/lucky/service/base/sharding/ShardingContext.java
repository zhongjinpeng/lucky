package com.lucky.service.base.sharding;

/**
 * 考虑到有些扫表类sql并不包含分表键，所以提供如下扩展类，通过ThreadLocal进行路由填充，当sql中不存在分表键时，使用该扩展类处理
 * @author <a href="mailto:jpzhong1994@gmail.com">zhongjinpeng</a>
 * @version 1.0
 * @date 2020/7/13 16:35
 *
 */
public class ShardingContext {

    /**
     * 分库key 库名+分库序号
     * 用于获取对应库名序号的dataSource
     * 使用分库插件时必须及时调用clear方法清空上下文
     */
    private static final ThreadLocal<String> SHARDING_DATABASE = new ThreadLocal<>();

    /**
     * 分表Key 表名后缀
     * 直接填充分表位，主要用于按表进行扫描，使用完成后必须及时调用clear方法清空上下文
     */
    private static final ThreadLocal<String> SHARDING_TABLE = new ThreadLocal<>();

    public static String getShardingDatabase() {
        return SHARDING_DATABASE.get();
    }

    public static void setShardingDatabase(String shardingDatabase) {
        SHARDING_DATABASE.set(shardingDatabase);
    }

    public static String getShardingTable() {
        return SHARDING_TABLE.get();
    }

    public static void setShardingTable(String shardingTable) {
        SHARDING_TABLE.set(shardingTable);
    }

    public static void clear() {
        ShardingContext.SHARDING_DATABASE.remove();
        ShardingContext.SHARDING_TABLE.remove();
    }

}
