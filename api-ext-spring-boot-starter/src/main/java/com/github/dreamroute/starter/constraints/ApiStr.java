package com.github.dreamroute.starter.constraints;

import com.github.dreamroute.starter.constraints.ApiStr.List;
import com.github.dreamroute.starter.constraints.validator.ApiStrValidator;

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
@ApiExtMarker
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
     * 是否必填，默认为：true，如果为true，那么就不能是<code>null</code>或者空字符串
     */
    boolean required() default true;

    /**
     * 是否隐藏
     */
    boolean hidden() default false;

    /**
     * 错误信息描述，无需填写，自动生成，需要required = true时才生效
     */
    String message() default "${name}不允许为空, 长度范围在${min}, ${max}之间";

    /**
     * 最小长度
     */
    int min();

    /**
     * 最大长度
     */
    int max();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({FIELD})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        ApiStr[] value();
    }
}
