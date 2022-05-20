package com.github.dreamroute.starter.constraints;

import com.github.dreamroute.starter.constraints.ApiExtDate.List;
import com.github.dreamroute.starter.constraints.validator.ApiExtDateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static com.github.dreamroute.starter.constraints.ApiExtDate.Phase.All;
import static com.github.dreamroute.starter.constraints.ApiExtMarker.BASE_MSG;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 描述：{@link java.util.Date}注解，用于限制日期
 *
 * @author w.dehi.2022-05-17
 */
@ApiExtMarker
@Target(FIELD)
@Retention(RUNTIME)
@Repeatable(List.class)
@Constraint(validatedBy = {ApiExtDateValidator.class})
public @interface ApiExtDate {

    enum Phase {
        /**
         * 不限制
         */
        All,

        /**
         * 未来
         */
        Future,

        /**
         * 现在或未来
         */
        FutureOrPresent,

        /**
         * 过去
         */
        Past,

        /**
         * 现在或过去
         */
        PastOrPresent
    }

    /**
     * 字段名称
     */
    String name();

    /**
     * 是否必填，默认为：true
     */
    boolean required() default true;

    /**
     * 是否隐藏
     */
    boolean hidden() default false;

    /**
     * 日期时间段，可以限制时间范围在：【过去】【现在或过去】【未来】【现在或未来】，默认{@link Phase#All}, 不限制
     */
    Phase phase() default All;


    /**
     * 错误信息描述，无需填写，自定义使用${}占位
     */
    String message() default BASE_MSG;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({FIELD})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        ApiExtDate[] value();
    }
}
