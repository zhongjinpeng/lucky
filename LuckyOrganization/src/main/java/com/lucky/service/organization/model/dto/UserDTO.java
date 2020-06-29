package com.lucky.service.organization.model.dto;

/**
 * @Author:
 * @Date:
 */

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: <a href="mailto:jpzhong1994@gmail.com">zhongjinpeng</a>
 * @version: v1.0
 */
@Api
@ApiModel
@Data
public class UserDTO implements Serializable {

    @ApiModelProperty(name = "用户姓名",value = "")
    private String username;

    @ApiModelProperty(name = "账号",value = "")
    private String account;

    @ApiModelProperty(name = "密码",value = "")
    private String password;

    @ApiModelProperty(name = "邮箱",value = "")
    private String email;

    @ApiModelProperty(name = "手机号",value = "")
    private String phone;

    @ApiModelProperty(name = "性别",value = "default 0,0男 1女")
    private Integer gender;
}
