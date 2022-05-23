package com.github.dreamroute.starter.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.XML;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import static cn.hutool.core.util.NumberUtil.isNumber;
import static com.google.common.collect.Lists.newArrayList;
import static java.util.Optional.ofNullable;

/**
 * 描述：为属性排序
 *
 * @author w.dehi.2022-05-23
 */
@Slf4j
@Aspect
@Component
public class SortProperties {

    @Around("execution(public * springfox.documentation.oas.web.WebMvcBasePathAndHostnameTransformationFilter.transform(..))")
    public Object process(ProceedingJoinPoint point) throws Throwable {
        Object[] args = point.getArgs();
        Object result = point.proceed(args);

        Collection<Schema> values = ((OpenAPI) result).getComponents().getSchemas().values();
        if (!values.isEmpty()) {
            for (Schema<?> value : values) {
                Map<String, Schema> properties = value.getProperties();
                ArrayList<Entry<String, Schema>> list = newArrayList(properties.entrySet());
                Collections.sort(list, (o1, o2) -> {
                    Object e1 = ofNullable(o1).map(Entry::getValue).map(Schema::getXml).map(XML::getName).orElse(null);
                    Object e2 = ofNullable(o2).map(Entry::getValue).map(Schema::getXml).map(XML::getName).orElse(null);

                    if (e1 != null && e2 != null && isNumber(e1.toString()) && isNumber(e2.toString())) {
                        return Integer.parseInt(e1.toString()) - Integer.parseInt(e2.toString());
                    }
                    return 0;
                });

                Map<String, Schema> sorted = new LinkedHashMap<>();
                list.forEach(e -> {
                    Schema<?> v = e.getValue();
                    v.setXml(null);
                    sorted.put(e.getKey(), v);
                });
                value.setProperties(sorted);
            }
        }
        return result;
    }
}