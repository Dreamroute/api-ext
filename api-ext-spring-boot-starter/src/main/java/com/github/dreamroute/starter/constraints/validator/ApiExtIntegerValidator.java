package com.github.dreamroute.starter.constraints.validator;

import com.alibaba.fastjson.JSONObject;
import com.github.dreamroute.starter.constraints.ApiExtInteger;
import lombok.Data;
import org.springframework.core.annotation.AnnotationUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Map;

/**
 * 描述：{@link Integer}校验器
 *
 * @author w.dehi.2022-05-17
 */
public class ApiExtIntegerValidator implements ConstraintValidator<ApiExtInteger, Integer> {

    private BaseAttr baseAttr;
    private Integer min;
    private Integer max;

    @Override
    public void initialize(ApiExtInteger anno) {
        Map<String, Object> attrs = AnnotationUtils.getAnnotationAttributes(anno);
        JSONObject jo = new JSONObject(attrs);
        baseAttr = jo.toJavaObject(BaseAttr.class);
        max = (Integer) attrs.get("max");
        min = (Integer) attrs.get("min");
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (baseAttr.required) {
            return value != null && value >= min && value <= max;
        } else {
            return value == null || (value >= min && value <= max);
        }
    }

    @Data
    private static class BaseAttr {
        private String name;
        private boolean required;
        private boolean hidden;
        private String message;
    }
}
