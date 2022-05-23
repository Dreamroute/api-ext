package com.githu.dreamroute.api.ext.sample.controller;

import com.githu.dreamroute.api.ext.sample.dto.UserDto.InsertReq;
import io.swagger.annotations.Api;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述：用户 Controller
 *
 * @author w.dehi.2022-05-18
 */
@RestController
@RequestMapping("/user")
@Api(tags = "用户相关操作")
public class UserController {

    @PostMapping("/insert")
    public InsertReq insert(@Validated @RequestBody InsertReq req) {
        return req;
    }

}
