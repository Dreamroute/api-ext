package com.github.dreamroute.starter.enums;

import com.github.dreamroute.starter.enums.ApiStr.List;
import com.github.dreamroute.starter.enums.validator.ApiStrValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 描述：字符串
 *
 * @author w.dehi.2022-05-17
 */
@Target(FIELD)
@Retention(RUNTIME)
@Repeatable(List.class)
@Constraint(validatedBy = {ApiStrValidator.class})
public @interface ApiStr {

    /**
     * 字段名称
     */
    String name();

    /**
     * 是否必填，默认：true
     */
    boolean required() default true;

    /**
     * 是否可以为空
     */
    boolean notEmpty() default false;

    /**
     * 最小长度
     */
    int min();

    /**
     * 最大长度
     */
    int max();

    /**
     * 是否隐藏，默认：false
     */
    boolean hidden() default false;

    /**
     * 错误信息描述，无需填写，自动生成
     */
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({FIELD})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        ApiStr[] value();
    }
}
