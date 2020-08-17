package com.lucky.service.organization.service;

import com.lucky.service.base.util.UUIDUtils;
import com.lucky.service.organization.dao.UserMapper;
import com.lucky.service.organization.model.entity.UserDO;
import com.lucky.service.organization.strategy.UserSharingTableStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author:
 * @Date:
 */
@Slf4j
@Component
public class UserServiceImpl {

    @Autowired
    private UserMapper userMapper;

    public void saveUser(UserDO userDO){
        userDO.setId(UUIDUtils.generateUUID32());
        userDO.setUserId(UUIDUtils.generateUUID32());
        userDO.setCreatedBy("system");
        userMapper.insertUser(userDO);
    }
}
