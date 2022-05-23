package com.github.dreamroute.starter.plugin;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.github.dreamroute.starter.constraints.ApiExtMarker;
import io.swagger.annotations.ApiModel;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import springfox.documentation.schema.Xml;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static springfox.documentation.swagger.common.SwaggerPluginSupport.pluginDoesApply;

/**
 * 描述：插件，填充原本<code>@ApiModelProperty</code>的基础属性
 *
 * @author w.dehi.2022-05-18
 */
@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 1) // 需要在ApiModelPropertyPropertyBuilder之后被调用，用于覆盖默认属性
public class FillBasePropertiesPlugin implements ModelPropertyBuilderPlugin {

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
                if (an != null && !CollectionUtils.isEmpty(API_EXT_ANNOS)) {
                    for (Class<?> apiExtAnno : API_EXT_ANNOS) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> annotationValueMap = AnnotationUtil.getAnnotationValueMap(field, (Class<Annotation>) apiExtAnno);
                        if (!CollectionUtils.isEmpty(annotationValueMap)) {
                            context.getSpecificationBuilder()
                                    .xml(new Xml().name(String.valueOf(getPosition(dtoCls, field.getName()))))
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