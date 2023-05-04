package com.githu.dreamroute.api.ext.sample.dto;

import com.github.dreamroute.mybatis.pro.base.codec.enums.EnumMarker;
import com.github.dreamroute.starter.constraints.*;
import com.github.dreamroute.starter.constraints.ApiExtDate.Phase;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 描述：用户 DTO
 *
 * @author w.dehi.2022-05-18
 */
public class UserDto {

    @Data
    @ApiModel("新增对象")
    public static class InsertReq {

        @ApiExtStr(name = "姓名", min = 2, max = 10, required = false)
        private String name;

        @ApiExtStr(name = "备注", max = 10, min = 2)
        private String remark;

        @ApiExtInteger(name = "年龄", max = 200, min = 1)
        private Integer age;

        @ApiExtLong(name = "头发根数", max = 100000L, min = 0L)
        private Long num;

        @ApiExtBigDecimal(name = "单价", max = "10.5", min = "0.2", required = false)
        private BigDecimal price;

        @ApiExtEnum(name = "状态", required = false)
        private Status status;

        @ApiExtDate(name = "出生日期", phase = Phase.PastOrPresent)
        private Date birthday;

        @ApiExtBoolean(name = "是否有效", required = false)
        private Boolean valid;

        @Valid
        @ApiExtArray(name = "角色列表")
        private Role[] roles;

        @Valid
        @ApiExtCollection(name = "角色信息")
        private List<Role> roleList;

        @ApiExtArray(name = "KPI列表", max = 2, required = false)
        private Boolean[] kpis;

        @ApiExtCollection(name = "地址")
        private List<String> addrs;

        @ApiExtCollection(name = "手机号码", min = 2, max = 5)
        private Set<String> phones;

        @ApiExtCollection(name = "状态列表")
        private Collection<Status> statuses;
    }

    @Data
    @ApiModel("新增返回对象")
    public static class InsertResp implements Serializable {
        @ApiExtResp("主键ID")
        private Long id;

        @ApiExtResp("姓名")
        private String name;

        @ApiExtResp("地址")
        private String[] addrs;

        @ApiExtResp("角色")
        private List<Role> roles;

        @ApiExtResp("状态")
        private Status status;
    }

    @Data
    public static class Role {
        @ApiExtStr(name = "角色名", max = 20, min = 2)
        private String name;
    }

    @Getter
    @RequiredArgsConstructor
    public enum Status implements EnumMarker {
        INIT(1, "初始化"),
        VALID(2, "有效"),
        DELETE(3, "已删除");

        private final Integer value;
        private final String desc;
    }

    @Data
    @ApiModel("新增商品对象")
    public static class ProductReq {

        @ApiModelProperty(value = "单价", required = true)
        @NotNull(message = "单价不能为空")
        @DecimalMin(value = "0", message = "单价最小0")
        @DecimalMax(value = "9999999999.9999", message = "单价不能超过100亿")
        private BigDecimal price;

    }

    @Data
    @ApiModel("新增商品对象")
    public static class ProductRequest {

        @ApiExtBigDecimal(name = "单价", max = "9999999999.9999", min = "0")
        private BigDecimal price;

    }

    @Data
    public static class Pk implements Serializable {
        @ApiExtLong(name = "主键ID")
        private Long id;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class CheckExtendsReq extends Pk implements Serializable {
        @ApiExtStr(name = "姓名", max = 10, required = false)
        private String name;
        @ApiExtStr(name = "密码", max = 16)
        private String password;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class CheckExtendsResp extends Pk implements Serializable {
        @ApiExtResp("姓名")
        private String name;
        @ApiExtResp("密码")
        private String password;
    }


}
