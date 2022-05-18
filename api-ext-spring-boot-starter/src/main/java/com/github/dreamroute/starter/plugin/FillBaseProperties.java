package com.github.dreamroute.starter.plugin;

import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.github.dreamroute.starter.constraints.ApiStr;
import io.swagger.annotations.ApiModel;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;

import static springfox.documentation.swagger.common.SwaggerPluginSupport.pluginDoesApply;

/**
 * 描述：插件，填充原本<code>@ApiModelProperty</code>的基础属性
 *
 * @author w.dehi.2022-05-18
 */
@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 1) // 需要在ApiModelPropertyPropertyBuilder之后被调用，用于覆盖默认属性
public class FillBaseProperties implements ModelPropertyBuilderPlugin {

    private static final ConcurrentHashMap<Class<?>, ConcurrentHashMap<Field, String>> CACHE = new ConcurrentHashMap<>();

    @Override
    public void apply(ModelPropertyContext context) {
        Class<?> erasedType = context.getOwner().getType().getErasedType();

        CACHE.computeIfAbsent(erasedType, cls -> {
            return null;
        });

        // 查找被@ApiModel标记的类，如果不这样ModelAndView也会被查到
        ApiModel apiModel = AnnotationUtils.findAnnotation(erasedType, ApiModel.class);
        if (apiModel != null) {
            context.getBeanPropertyDefinition().ifPresent(x -> {
                AnnotatedField field = x.getField();
                Field annotated = field.getAnnotated();
                ApiStr an = AnnotationUtils.findAnnotation(annotated, ApiStr.class);
                if (an != null) {
                    context.getSpecificationBuilder()
                            .nullable(!an.required())
                            .description(an.name())
                            .required(an.required());
                }
            });
        }
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return pluginDoesApply(delimiter);
    }
}
