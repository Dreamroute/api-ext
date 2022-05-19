package com.github.dreamroute.starter.constraints.validator;

import com.alibaba.fastjson.JSONObject;
import com.github.dreamroute.starter.constraints.ApiExtLong;
import lombok.Data;
import org.springframework.core.annotation.AnnotationUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Map;

/**
 * 描述：{@link Long}校验器
 *
 * @author w.dehi.2022-05-17
 */
public class ApiExtLongValidator implements ConstraintValidator<ApiExtLong, Long> {

    private BaseAttr baseAttr;
    private Long min;
    private Long max;

    @Override
    public void initialize(ApiExtLong anno) {
        Map<String, Object> attrs = AnnotationUtils.getAnnotationAttributes(anno);
        JSONObject jo = new JSONObject(attrs);
        baseAttr = jo.toJavaObject(BaseAttr.class);
        max = (Long) attrs.get("max");
        min = (Long) attrs.get("min");
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
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
