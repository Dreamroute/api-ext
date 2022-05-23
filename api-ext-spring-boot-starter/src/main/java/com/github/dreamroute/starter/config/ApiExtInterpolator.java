package com.github.dreamroute.starter.config;

import com.github.dreamroute.starter.constraints.ApiExtArray;
import com.github.dreamroute.starter.constraints.ApiExtCollection;
import com.github.dreamroute.starter.constraints.ApiExtDate;
import com.github.dreamroute.starter.constraints.ApiExtDate.Phase;
import com.github.dreamroute.starter.constraints.ApiExtStr;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import org.springframework.util.PropertyPlaceholderHelper;

import javax.validation.MessageInterpolator;
import java.lang.annotation.Annotation;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import static com.github.dreamroute.starter.constraints.ApiExtMarker.BASE_MSG;
import static com.github.dreamroute.starter.plugin.FillBasePropertiesPlugin.API_EXT_ANNOS;

/**
 * @author w.dehi.2022-05-17
 */
public class ApiExtInterpolator implements MessageInterpolator {

    private final MessageInterpolator targetInterpolator;

    private static final String NOT_EMPTY = "不允许为空";

    public ApiExtInterpolator(MessageInterpolator targetInterpolator) {
        Assert.notNull(targetInterpolator, "Target MessageInterpolator must not be null");
        this.targetInterpolator = targetInterpolator;
    }

    @Override
    public String interpolate(String message, Context context) {
        Annotation apiExtAnnotation = context.getConstraintDescriptor().getAnnotation();

        // 只处理被@ApiExtMarker标记过的注解
        Class<? extends Annotation> apiExt = apiExtAnnotation.annotationType();
        if (API_EXT_ANNOS.contains(apiExt)) {
            Map<String, Object> attrs = AnnotationUtils.getAnnotationAttributes(apiExtAnnotation);
            Properties properties = new Properties();
            attrs.forEach((k, v) -> properties.put(k.toString(), v.toString()));
            boolean required = (boolean) attrs.get("required");
            if (required) {
                properties.put("required", NOT_EMPTY + ",");
            } else {
                properties.put("required","");
            }
            PropertyPlaceholderHelper helper = new PropertyPlaceholderHelper("${", "}");
            String result = helper.replacePlaceholders(message, properties);

            // 移除末尾逗号
            if (result.endsWith(NOT_EMPTY + ",")) {
                result = result.replace(NOT_EMPTY + ",", NOT_EMPTY);
            }

            // ---------对于错误信息不太好处理的就放在这里做特殊处理
            // 日期类型
            if (apiExt == ApiExtDate.class) {
                result = processDateMsg(attrs, result);
            }
            // 数组和集合类型
            else if ((apiExt == ApiExtStr.class || apiExt == ApiExtArray.class || apiExt == ApiExtCollection.class) && !required) {
                result = helper.replacePlaceholders(BASE_MSG, properties) + "数量必须小于" + attrs.get("max");
            }
            // -----------

            return result;
        }
        return this.targetInterpolator.interpolate(message, context, LocaleContextHolder.getLocale());
    }

    private String processDateMsg(Map<String, Object> attrs, String result) {
        switch ((Phase) attrs.get("phase")) {
            case Past: result += ", 并且需要小于当前时间"; break;
            case PastOrPresent: result += ", 并且需要小于等于当前时间"; break;
            case Future: result += ", 并且需要大于当前时间"; break;
            case FutureOrPresent: result += ", 并且需要大于等于当前时间"; break;
            default: {}
        }
        return result;
    }

    @Override
    public String interpolate(String message, Context context, Locale locale) {
        return this.targetInterpolator.interpolate(message, context, locale);
    }

}
