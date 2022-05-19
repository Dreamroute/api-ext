package com.githu.dreamroute.api.ext.sample.dto;

import com.github.dreamroute.starter.constraints.ApiExtBigDecimal;
import com.github.dreamroute.starter.constraints.ApiExtInteger;
import com.github.dreamroute.starter.constraints.ApiExtLong;
import com.github.dreamroute.starter.constraints.ApiExtObject;
import com.github.dreamroute.starter.constraints.ApiExtStr;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 描述：用户 DTO
 *
 * @author w.dehi.2022-05-18
 */
public class UserDto {

    @Data
    @ApiModel("新增对象")
    public static class InsertReq {

        @ApiExtStr(name = "姓名", max = 10, min = 2)
        private String name;

        @ApiExtStr(name = "备注", max = 10, min = 2)
        private String remark;

        @ApiExtInteger(name = "年龄", max = 200, min = 1)
        private Integer age;

        @ApiExtLong(name = "头发根数", max = 100000L, min = 0L)
        private Long num;

        @ApiExtBigDecimal(name = "单价", max = "10.5", min = "0.2", required = false)
        private BigDecimal price;

        @ApiExtObject(name = "状态")
        private Status status;

        @Valid
        @NotEmpty
        private Role[] roles;
    }

    @Data
    public static class Role {
        @NotNull(message = "角色名不能为空")
        private String name;
    }

    public enum Status {
        INIT,
        VALID,
        DELETE
    }
}
