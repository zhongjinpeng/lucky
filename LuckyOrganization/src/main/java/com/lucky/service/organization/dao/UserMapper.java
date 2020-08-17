package com.lucky.service.organization.dao;

import com.lucky.service.base.annotation.Sharding;
import com.lucky.service.organization.model.entity.UserDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author <a href="mailto:jpzhong1994@gmail.com">zhongjinpeng</a>
 * @version v1.0
 */
@Mapper
@Sharding(sharding = true, createTable = true, shardingKey = "createdTime", databaseName = "lucky_organization", tableName = "user", strategy = "com.lucky.service.organization.strategy.UserSharingTableStrategy", count = 0)
public interface UserMapper {

    void insertUser(UserDO userDO);
}
