package com.lucky.service.base.model;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author: <a href="mailto:jpzhong1994@gmail.com">zhongjinpeng</a>
 * @version: v1.0
 */
@Api
@ApiModel
@Data
public class BaseEntity implements Serializable {

    @ApiModelProperty(name = "创建时间", value="")
    private Timestamp createdTime;

    @ApiModelProperty(name = "修改时间", value="")
    private Timestamp updatedTime;

    @ApiModelProperty(name = "创建人", value="")
    private String createdBy;

    @ApiModelProperty(name = "修改人", value="")
    private String updatedBy;

    @ApiModelProperty(name = "是否删除", value="default 0,0否 1是")
    private Integer isDel;

}
