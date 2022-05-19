package com.github.dreamroute.starter.constraints.validator;

import com.alibaba.fastjson.JSONObject;
import com.github.dreamroute.starter.constraints.ApiExtBigDecimal;
import lombok.Data;
import org.springframework.core.annotation.AnnotationUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 描述：{@link Integer}校验器
 *
 * @author w.dehi.2022-05-17
 */
public class ApiExtBigDecimalValidator implements ConstraintValidator<ApiExtBigDecimal, BigDecimal> {

    private BaseAttr baseAttr;
    private BigDecimal min;
    private BigDecimal max;

    @Override
    public void initialize(ApiExtBigDecimal anno) {
        Map<String, Object> attrs = AnnotationUtils.getAnnotationAttributes(anno);
        JSONObject jo = new JSONObject(attrs);
        baseAttr = jo.toJavaObject(BaseAttr.class);
        max = new BigDecimal( String.valueOf(attrs.get("max")));
        min = new BigDecimal(String.valueOf(attrs.get("min")));
    }

    @Override
    public boolean isValid(BigDecimal value, ConstraintValidatorContext context) {
        if (baseAttr.required) {
            return value != null && value.compareTo(min) >= 0 && value.compareTo(max) <= 0;
        } else {
            return value == null || (value.compareTo(min) >= 0 && value.compareTo(max) <= 0);
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
