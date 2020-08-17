package com.lucky.service.organization.strategy;

import com.lucky.service.base.annotation.Sharding;
import com.lucky.service.base.sharding.strategy.table.AbstractShardingTableStrategy;
import com.lucky.service.base.sharding.strategy.table.DefaultShardingTableStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * 按月
 * @author <a href="mailto:jpzhong1994@gmail.com">zhongjinpeng</a>
 * @version 1.0
 * @date 2020/7/14 16:20
 *
 */
public class UserSharingTableStrategy extends AbstractShardingTableStrategy {

    private static final Logger logger = LoggerFactory.getLogger(UserSharingTableStrategy.class);

    @Override
    public String getCreateTableSql() {
        return "CREATE TABLE IF NOT EXISTS `user`  (\n" +
                "  `id` varchar(36) NOT NULL,\n" +
                "  `user_id` varchar(36) NOT NULL COMMENT '用户id',\n" +
                "  `username` varchar(45) DEFAULT '' COMMENT '真实姓名',\n" +
                "  `account` varchar(36) DEFAULT '' COMMENT '账号',\n" +
                "  `password` varchar(100) DEFAULT '' COMMENT '密码',\n" +
                "  `email` varchar(30) DEFAULT '' COMMENT '用户邮箱',\n" +
                "  `phone` varchar(13) DEFAULT '' COMMENT '手机号码',\n" +
                "  `gender` tinyint DEFAULT '0' COMMENT '性别 0男 1女',\n" +
                "  `created_time` timestamp NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建的时间',\n" +
                "  `updated_time` timestamp NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改的时间',\n" +
                "  `created_by` varchar(30) DEFAULT ''  COMMENT '创建人',\n" +
                "  `updated_by` varchar(30) DEFAULT ''  COMMENT '修改人',\n" +
                "  `is_del` tinyint DEFAULT '0' COMMENT '是否删除 0否 1是',\n" +
                "  PRIMARY KEY (`id`),\n" +
                "  UNIQUE KEY `idx_user_id` (`user_id`),\n" +
                "  UNIQUE KEY `idx_account` (`account`),\n" +
                "  UNIQUE KEY `idx_phone` (`phone`),\n" +
                "  KEY `idx_username` (`username`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户信息表';\n";

    }

    @Override
    public Integer calculateTableSuffix(Sharding sharding, Object shardingKey) {
        logger.info("------shardingKey:{}",shardingKey);
        Calendar calender = Calendar.getInstance();
        if(Objects.nonNull(shardingKey)) {
            calender = (Calendar)shardingKey;
        }
        return calender.get(Calendar.MONTH) + 1;
    }
}
