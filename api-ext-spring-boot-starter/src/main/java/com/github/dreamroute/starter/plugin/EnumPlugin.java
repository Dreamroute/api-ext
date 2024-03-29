package com.github.dreamroute.starter.plugin;

import org.springframework.plugin.core.Plugin;

/**
 * 处理swagger枚举类型的描述信息，后端开发人员无需编写对应的描述，而是通过枚举自动生成
 */
public interface EnumPlugin extends Plugin<Class<?>> {

    /**
     * 如果返回单个描述，那么就直接返回字符串，如果返回枚举类型，那么使用逗号分割，比如"1-有效,2-无效"
     *
     * @param customType 自定义类型
     */
    String[] desc(Class<?> customType);

    /**
     * 展示给前端的枚举字段需要传递的类型，比如integer, string
     */
    String enumType();

    /**
     * 展示给前端的枚举字段需要传递的类型的格式，比如int32, int64，一般来说只有数字类型需要，其他的返回null即可
     */
    String enumFormat();

}
