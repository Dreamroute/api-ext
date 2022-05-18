package com.github.dreamroute.starter.config;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.Assert;

import javax.validation.MessageInterpolator;
import java.lang.annotation.Annotation;
import java.util.Locale;

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
        Annotation annotation = context.getConstraintDescriptor().getAnnotation();
        StringBuilder builder = new StringBuilder();
//        if (annotation instanceof ApiStr) {
//            ApiStr as = (ApiStr) annotation;
//            builder.append(as.name());
//            String name = as.name();
//            if (as.notNull()) {
//                builder.append("不能为空, ");
//            }
//            builder.append("长度范围是: [").append(as.min()).append(", ").append(as.max()).append("]");
//            return builder.toString();
//        } else {
//            return this.targetInterpolator.interpolate(message, context, LocaleContextHolder.getLocale());
//        }
        return this.targetInterpolator.interpolate(message, context, LocaleContextHolder.getLocale());
    }

    @Override
    public String interpolate(String message, Context context, Locale locale) {
        return this.targetInterpolator.interpolate(message, context, locale);
    }

}
