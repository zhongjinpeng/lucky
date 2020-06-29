package com.lucky.service.organization.dao;

import com.lucky.service.organization.model.entity.UserDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: <a href="mailto:jpzhong1994@gmail.com">zhongjinpeng</a>
 * @version: v1.0
 */
@Mapper
public interface UserMapper {

    void insertUser(UserDO userDO);
}
