package com.githu.dreamroute.api.ext.sample.controller;

import com.githu.dreamroute.api.ext.sample.dto.UserDto.CheckExtendsReq;
import com.githu.dreamroute.api.ext.sample.dto.UserDto.CheckExtendsResp;
import com.githu.dreamroute.api.ext.sample.dto.UserDto.InsertReq;
import com.githu.dreamroute.api.ext.sample.dto.UserDto.InsertResp;
import io.swagger.annotations.Api;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.githu.dreamroute.api.ext.sample.dto.UserDto.Status.DELETE;

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
    public InsertResp insert(@Validated @RequestBody InsertReq req) {
        InsertResp resp = new InsertResp();
        BeanUtils.copyProperties(req, resp);
        return resp;
    }

    /**
     * 检查下swagger文档请求和返回值带有继承的实体是如何显示的
     */
    @PostMapping("/checkExtends")
    public List<CheckExtendsResp> checkExtends(@Validated @RequestBody CheckExtendsReq req) {
        return null;
    }

}
