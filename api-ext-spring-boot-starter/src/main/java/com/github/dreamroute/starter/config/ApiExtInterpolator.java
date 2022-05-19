package com.github.dreamroute.starter.config;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import org.springframework.util.PropertyPlaceholderHelper;

import javax.validation.MessageInterpolator;
import java.lang.annotation.Annotation;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import static com.github.dreamroute.starter.plugin.FillBaseProperties.API_EXT_ANNOS;

/**
 * @author w.dehi.2022-05-17
 */
public class ApiExtInterpolator implements MessageInterpolator {

    private final MessageInterpolator targetInterpolator;

    public ApiExtInterpolator(MessageInterpolator targetInterpolator) {
        Assert.notNull(targetInterpolator, "Target MessageInterpolator must not be null");
        this.targetInterpolator = targetInterpolator;
    }

    @Override
    public String interpolate(String message, Context context) {
        Annotation apiExtAnnotation = context.getConstraintDescriptor().getAnnotation();

        // 只处理被@ApiExtMarker标记过的注解
        if (API_EXT_ANNOS.contains(apiExtAnnotation.annotationType())) {
            Map<String, Object> attrs = AnnotationUtils.getAnnotationAttributes(apiExtAnnotation);
            Properties properties = new Properties();
            attrs.forEach((k, v) -> properties.put(k.toString(), v.toString()));
            boolean required = (boolean) attrs.get("required");
            if (required) {
                properties.put("required", "不允许为空, ");
            } else {
                properties.put("required", "");
            }
            PropertyPlaceholderHelper helper = new PropertyPlaceholderHelper("${", "}");
            return helper.replacePlaceholders(message, properties);
        }
        return this.targetInterpolator.interpolate(message, context, LocaleContextHolder.getLocale());
    }

    @Override
    public String interpolate(String message, Context context, Locale locale) {
        return this.targetInterpolator.interpolate(message, context, locale);
    }

}
