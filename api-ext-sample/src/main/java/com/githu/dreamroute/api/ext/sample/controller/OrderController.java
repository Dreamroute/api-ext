package com.githu.dreamroute.api.ext.sample.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述：// TODO
 *
 * @author w.dehi.2022-05-25
 */
@Api(tags = "订单相关操作")
@RestController
@RequestMapping("/order")
public class OrderController {

    @ApiOperation(value = "根据ID查询")
    @GetMapping("/select_by_id")
    void selectById(Long id) {}

    @ApiOperation(value = "订单列表")
    @GetMapping("/list_orders")
    void listOrders() {}

    @ApiOperation(value = "删除")
    @GetMapping("/delete")
    void delete() {}

    @ApiOperation(value = "新增")
    @GetMapping("/insert")
    void insert() {}
}
