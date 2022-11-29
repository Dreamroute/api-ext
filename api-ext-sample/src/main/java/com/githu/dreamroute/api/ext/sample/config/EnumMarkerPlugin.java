package com.githu.dreamroute.api.ext.sample.config;

import com.github.dreamroute.mybatis.pro.base.codec.enums.EnumMarker;
import com.github.dreamroute.starter.plugin.EnumPlugin;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 枚举类型的描述信息，显示成"1-有效；2-无效"这种
 */
@Component
public class EnumMarkerPlugin implements EnumPlugin {

    @Override
    public String[] desc(Class<?> customType) {
        @SuppressWarnings("unchecked")
        Class<EnumMarker> c = (Class<EnumMarker>) customType;
        EnumMarker[] enumConstants = c.getEnumConstants();
        if (enumConstants != null && enumConstants.length > 0) {
            return Arrays.stream(enumConstants).map(e -> e.getValue() + "-" + e.getDesc()).toArray(String[]::new);
        }
        return new String[0];
    }

    @Override
    public boolean supports(Class<?> delimiter) {
        return EnumMarker.class.isAssignableFrom(delimiter);
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
