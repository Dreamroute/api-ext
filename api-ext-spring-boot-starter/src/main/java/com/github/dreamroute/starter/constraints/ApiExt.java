package com.github.dreamroute.starter.constraints;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 描述：{@link String}注解，用于限制字符串
 *
 * @author w.dehi.2022-05-17
 */
@ApiExtMarker
@Target(FIELD)
@Retention(RUNTIME)
public @interface ApiExt {

    /**
     * 属性名称
     */
    String value();

    /**
     * 是否隐藏
     */
    boolean hidden() default false;

}
