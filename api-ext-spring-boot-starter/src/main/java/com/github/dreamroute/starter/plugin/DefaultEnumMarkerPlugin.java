package com.github.dreamroute.starter.plugin;

import org.springframework.stereotype.Component;

/**
 * 枚举类型的描述信息，显示成"1-有效；2-无效"这种
 */
@Component
public class DefaultEnumMarkerPlugin implements EnumPlugin {

    @Override
    public String[] desc(Class<?> customType) {
        return new String[0];
    }

    @Override
    public boolean supports(Class<?> delimiter) {
        return DefaultType.class.isAssignableFrom(delimiter);
    }

    @Override
    public String enumType() {
        return "integer";
    }

    @Override
    public String enumFormat() {
        return "int32";
    }
}
