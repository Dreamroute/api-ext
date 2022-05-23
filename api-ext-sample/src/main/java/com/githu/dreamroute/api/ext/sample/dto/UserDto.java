package com.githu.dreamroute.api.ext.sample.dto;

import com.github.dreamroute.mybatis.pro.base.codec.enums.EnumMarker;
import com.github.dreamroute.starter.constraints.ApiExt;
import com.github.dreamroute.starter.constraints.ApiExtArray;
import com.github.dreamroute.starter.constraints.ApiExtBigDecimal;
import com.github.dreamroute.starter.constraints.ApiExtCollection;
import com.github.dreamroute.starter.constraints.ApiExtDate;
import com.github.dreamroute.starter.constraints.ApiExtDate.Phase;
import com.github.dreamroute.starter.constraints.ApiExtInteger;
import com.github.dreamroute.starter.constraints.ApiExtLong;
import com.github.dreamroute.starter.constraints.ApiExtObject;
import com.github.dreamroute.starter.constraints.ApiExtStr;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
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

        @ApiModelProperty(allowableValues = "5, 5, 5")
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

        @ApiExtObject(name = "状态", required = false)
        private Status status;

        @ApiExtDate(name = "出生日期", phase = Phase.PastOrPresent)
        private Date birthday;

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
    }

    @Data
    @ApiModel("新增返回对象")
    public static class InsertResp implements Serializable {
        @ApiExt("主键ID")
        private Long id;

        @ApiExt("姓名")
        private String name;

        @ApiExt("地址")
        private String[] addrs;

        @ApiExt("角色")
        private List<Role> roles;

        @ApiExt("状态")
        @ApiModelProperty(allowableValues = "1, 2, 3")
        private Status status;
    }

    @Data
    @ApiModel
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


}
