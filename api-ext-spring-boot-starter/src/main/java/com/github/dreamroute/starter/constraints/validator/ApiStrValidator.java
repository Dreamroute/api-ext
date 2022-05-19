package com.github.dreamroute.starter.constraints.validator;

import com.alibaba.fastjson.JSONObject;
import com.github.dreamroute.starter.constraints.ApiStr;
import lombok.Data;
import org.springframework.core.annotation.AnnotationUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Map;

/**
 * 描述：字符串校验器
 *
 * @author w.dehi.2022-05-17
 */
public class ApiStrValidator implements ConstraintValidator<ApiStr, String> {

    private BaseAttr baseAttr;
    private int min;
    private int max;

    @Override
    public void initialize(ApiStr anno) {
        Map<String, Object> attrs = AnnotationUtils.getAnnotationAttributes(anno);
        JSONObject jo = new JSONObject(attrs);
        baseAttr = jo.toJavaObject(BaseAttr.class);
        max = (int) attrs.get("max");
        min = (int) attrs.get("min");
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (baseAttr.required) {
            return value != null && (value = value.trim()).length() >= min && value.length() <= max;
        }
        return false;
    }

    @Data
    private static class BaseAttr {
        private String name;
        private boolean required;
        private boolean hidden;
        private String message;
    }
}
