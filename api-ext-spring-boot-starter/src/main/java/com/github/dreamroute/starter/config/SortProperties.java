package com.github.dreamroute.starter.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.XML;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static cn.hutool.core.util.NumberUtil.isNumber;
import static com.github.dreamroute.starter.plugin.FillBasePropertiesPlugin.SPECIAL;
import static com.google.common.collect.Lists.newArrayList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

/**
 * 描述：为属性排序
 *
 * @author w.dehi.2022-05-23
 */
@Slf4j
@Aspect
public class SortProperties {

    @SuppressWarnings("rawtypes")
    @Around("execution(public * springfox.documentation.oas.web.WebMvcBasePathAndHostnameTransformationFilter.transform(..))")
    public Object process(ProceedingJoinPoint point) throws Throwable {
        Object[] args = point.getArgs();
        Object result = point.proceed(args);

        Collection<Schema> values = ((OpenAPI) result).getComponents().getSchemas().values();
        if (!values.isEmpty()) {
            for (Schema<?> value : values) {
                Map<String, Schema> properties = value.getProperties();
                List<Entry<String, Schema>> list = newArrayList(properties.entrySet());
                list.sort((o1, o2) -> {
                    Object e1 = ofNullable(o1).map(Entry::getValue).map(Schema::getXml).map(XML::getName).orElse(null);
                    Object e2 = ofNullable(o2).map(Entry::getValue).map(Schema::getXml).map(XML::getName).orElse(null);

                    if (e1 != null && e2 != null && isNumber(e1.toString()) && isNumber(e2.toString())) {
                        return Integer.parseInt(e1.toString()) - Integer.parseInt(e2.toString());
                    }
                    return 0;
                });

                // 转成linkedhashmap才能保证value排序的正确性
                Map<String, Schema> sorted = new LinkedHashMap<>();

                list.forEach(e -> {
                    Schema<?> v = e.getValue();

                    String namespace = ofNullable(v.getXml()).map(XML::getNamespace).orElse("");
                    if (!StringUtils.isEmpty(namespace) && namespace.startsWith(SPECIAL) && namespace.endsWith(SPECIAL)) {
                        String[] enums = namespace.substring(SPECIAL.length(), namespace.length() - SPECIAL.length()).split(",");
                        List<String> collect = Arrays.stream(enums).collect(toList());

                        if (v instanceof ArraySchema) {
                            ((ArraySchema) e.getValue()).getItems().setEnum((List) collect);
                        } else {
                            v.setEnum((List) collect);
                        }
                    }
                    v.setXml(null);
                    sorted.put(e.getKey(), v);
                });
                value.setProperties(sorted);
            }
        }
        return result;
    }
}