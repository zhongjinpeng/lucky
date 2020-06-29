package com.lucky.service.organization.controller;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import com.lucky.service.organization.model.dto.UserDTO;
import com.lucky.service.organization.model.entity.UserDO;
import com.lucky.service.organization.service.UserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: <a href="mailto:jpzhong1994@gmail.com">zhongjinpeng</a>
 * @version: v1.0
 */
@Api
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @RequestMapping(method = RequestMethod.POST,value = "/insert")
    public void saveUser(@ApiParam @RequestBody UserDTO userDTO){
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(userDTO,userDO);
        userService.saveUser(userDO);
    }
}
