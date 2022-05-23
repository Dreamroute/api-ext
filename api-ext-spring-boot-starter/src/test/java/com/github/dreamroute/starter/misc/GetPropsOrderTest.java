package com.github.dreamroute.starter.misc;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static java.util.Optional.ofNullable;

/**
 * 描述：反射获取属性顺序
 *
 * @author w.dehi.2022-05-23
 */
class GetPropsOrderTest {

    @Test
    void getOrderTest() {
        Field[] fields = ReflectUtil.getFields(User.class);
        for (Field field : fields) {
            System.err.println(field.getName());
        }
    }

    @Data
    public static class User {
        private String c;
        private String a;
        private String b;
    }

}
