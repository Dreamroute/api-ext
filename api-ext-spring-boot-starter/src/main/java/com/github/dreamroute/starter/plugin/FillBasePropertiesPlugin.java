package com.github.dreamroute.starter.plugin;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.github.dreamroute.mybatis.pro.base.codec.enums.EnumMarker;
import com.github.dreamroute.starter.constraints.ApiExtMarker;
import com.github.dreamroute.starter.constraints.ApiExtResp;
import io.swagger.annotations.ApiModel;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import springfox.documentation.builders.ModelSpecificationBuilder;
import springfox.documentation.schema.ScalarType;
import springfox.documentation.schema.Xml;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.stream.Collectors.joining;
import static springfox.documentation.swagger.common.SwaggerPluginSupport.pluginDoesApply;

/**
 * 描述：插件，填充原本<code>@ApiModelProperty</code>的基础属性
 *
 * @author w.dehi.2022-05-18
 */
@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 1) // 需要在ApiModelPropertyPropertyBuilder之后被调用，用于覆盖默认属性
public class FillBasePropertiesPlugin implements ModelPropertyBuilderPlugin {

    public static final String SPECIAL = "_____";

    private static final Map<Class<?>, Map<String, Integer>> ORDER_CACHE = new ConcurrentHashMap<>();
    public static final Set<Class<?>> API_EXT_ANNOS;
    static {
        API_EXT_ANNOS = ClassUtil.scanPackageByAnnotation(ApiExtMarker.class.getPackage().getName(), ApiExtMarker.class);
    }

    private int getPosition(Class<?> dtoCls, String name) {
        Map<String, Integer> map = ORDER_CACHE.get(dtoCls);
        if (map == null || map.isEmpty()) {
            map = new ConcurrentHashMap<>();
            ORDER_CACHE.put(dtoCls, map);
            Field[] fields = ReflectUtil.getFields(dtoCls);
            for (int i = 0; i < fields.length; i++) {
                map.put(fields[i].getName(), i);
            }
        }
        return map.get(name);
    }

    @Override
    public void apply(ModelPropertyContext context) {
        Class<?> dtoCls = context.getOwner().getType().getErasedType();

        // 查找被@ApiModel标记的DTO类，如果不这样ModelAndView也会被查到
        ApiModel apiModel = AnnotationUtils.findAnnotation(dtoCls, ApiModel.class);

        if (apiModel != null) {
            context.getBeanPropertyDefinition().ifPresent(x -> {
                AnnotatedField af = x.getField();
                Field field = af.getAnnotated();
                ApiExtMarker an = AnnotationUtils.findAnnotation(field, ApiExtMarker.class);
                if (an != null) {
                    // 使用xml来代表顺序，最后再将其设置成null
                    context.getSpecificationBuilder()
                            .xml(new Xml().name(String.valueOf(getPosition(dtoCls, field.getName()))));
                    if (EnumMarker.class.isAssignableFrom(field.getType())) {
                        @SuppressWarnings("unchecked")
                        Class<EnumMarker> c = (Class<EnumMarker>) field.getType();
                        EnumMarker[] enumConstants = c.getEnumConstants();
                        if (enumConstants != null && enumConstants.length > 0) {
                            String desc = Arrays.stream(enumConstants).map(e -> e.getValue() + "-" + e.getDesc()).collect(joining(","));
                            context.getSpecificationBuilder()
                                    .xml(new Xml()
                                            .name(String.valueOf(getPosition(dtoCls, field.getName())))
                                            .namespace(SPECIAL + desc + SPECIAL))
                                    // 将枚举类型设置成Integer，前端看到的数据类型才是"integer($int32)"，否则就是string类型
                                    .type(new ModelSpecificationBuilder().scalarModel(ScalarType.INTEGER).build());
                        }
                    }
                }

                // 如果是返回参数
                ApiExtResp apiExtResp = AnnotationUtils.findAnnotation(field, ApiExtResp.class);
                if (apiExtResp != null) {
                    context.getSpecificationBuilder()
                            .description(apiExtResp.value())
                            .isHidden(apiExtResp.hidden());
                }
                // 如果是请求参数
                else if (an != null && !CollectionUtils.isEmpty(API_EXT_ANNOS)) {
                    for (Class<?> apiExtAnno : API_EXT_ANNOS) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> annotationValueMap = AnnotationUtil.getAnnotationValueMap(field, (Class<Annotation>) apiExtAnno);
                        if (!CollectionUtils.isEmpty(annotationValueMap)) {
                            context.getSpecificationBuilder()
                                    .description((String) annotationValueMap.get("name"))
                                    .required((Boolean) annotationValueMap.get("required"))
                                    .isHidden((Boolean) annotationValueMap.get("hidden"));
                            break;
                        }
                    }
                }
            });
        }
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return pluginDoesApply(delimiter);
    }
}
