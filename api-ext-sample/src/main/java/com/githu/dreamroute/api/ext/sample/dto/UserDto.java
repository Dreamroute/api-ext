package com.githu.dreamroute.api.ext.sample.dto;

import com.github.dreamroute.starter.constraints.ApiStr;
import io.swagger.annotations.ApiModel;
import lombok.Data;

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

        @ApiStr(name = "姓名", max = 10, min = 2)
        private String name;

        @ApiStr(name = "备注", max = 10, min = 2, required = false)
        private String reamark;

        @NotNull(message = "年龄不能为空")
        private Integer age;

        private BigDecimal price;

        private Status status;
    }

    public enum Status {
        INIT,
        VALID,
        DELETE
    }
}
