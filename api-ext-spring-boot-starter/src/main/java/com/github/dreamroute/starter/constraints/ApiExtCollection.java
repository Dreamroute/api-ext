package com.github.dreamroute.starter.constraints;

import com.github.dreamroute.starter.constraints.ApiExtCollection.List;
import com.github.dreamroute.starter.constraints.validator.ApiExtCollectionValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static com.github.dreamroute.starter.constraints.ApiExtMarker.BASE_MSG;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 描述：{@link java.util.Collection}注解，校验 {@link java.util.List}和{@link java.util.Set}
 *
 * @author w.dehi.2022-05-17
 */
@ApiExtMarker
@Target(FIELD)
@Retention(RUNTIME)
@Repeatable(List.class)
@Constraint(validatedBy = {ApiExtCollectionValidator.class})
public @interface ApiExtCollection {

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
     * 数组元素最小个数
     */
    int min() default 1;

    /**
     * 数组元素最大个数
     */
    int max() default Integer.MAX_VALUE;

    /**
     * 错误信息描述，无需填写，自定义使用${}占位
     */
    String message() default BASE_MSG + "数量范围在[${min}至${max}]之间";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({FIELD})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        ApiExtCollection[] value();
    }
}
