package com.github.dreamroute.starter.constraints;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 描述：标记是api-ext的注解，api-ext自定义的参数校验都需要被此注解标注
 *
 * @author w.dehi.2022-05-19
 */
@Inherited
@Target(TYPE)
@Retention(RUNTIME)
public @interface ApiExtMarker {

    String BASE_MSG = "[${name}]${required}";

}
