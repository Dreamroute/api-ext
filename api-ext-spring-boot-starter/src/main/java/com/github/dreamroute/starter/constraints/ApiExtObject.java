package com.github.dreamroute.starter.constraints;

import com.github.dreamroute.starter.constraints.ApiExtObject.List;
import com.github.dreamroute.starter.constraints.validator.ApiExtObjectValidator;

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
 * 描述：{@link Object}注解，用于限制字符串
 *
 * @author w.dehi.2022-05-17
 */
@ApiExtMarker
@Target(FIELD)
@Retention(RUNTIME)
@Repeatable(List.class)
@Constraint(validatedBy = {ApiExtObjectValidator.class})
public @interface ApiExtObject {

    /**
     * 字段名称
     */
    String name();

    /**
     * 是否必填，默认为：true，如果为true，那么就不能是<code>null</code>或者空字符串
     */
    boolean required() default true;

    /**
     * 是否隐藏
     */
    boolean hidden() default false;

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
        ApiExtObject[] value();
    }
}
