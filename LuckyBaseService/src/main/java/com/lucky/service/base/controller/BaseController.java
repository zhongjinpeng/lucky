package com.lucky.service.base.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 *
 * @author: <a href="mailto:jpzhong1994@gmail.com">zhongjinpeng</a>
 * @version: v1.0
 */
@Api
@Slf4j
@RestController
@RequestMapping("/base")
public class BaseController {

    @RequestMapping(method = RequestMethod.GET,value = "")
    @ApiOperation(value = "", notes = "index")
    public void index() {
    }

}
