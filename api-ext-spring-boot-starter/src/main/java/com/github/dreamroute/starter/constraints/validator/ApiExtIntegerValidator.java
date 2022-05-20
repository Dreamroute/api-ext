package com.github.dreamroute.starter.constraints.validator;

import com.github.dreamroute.starter.constraints.ApiExtInteger;
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

    private boolean required;
    private Integer min;
    private Integer max;

    @Override
    public void initialize(ApiExtInteger anno) {
        Map<String, Object> attrs = AnnotationUtils.getAnnotationAttributes(anno);
        max = (Integer) attrs.get("max");
        min = (Integer) attrs.get("min");
        required = anno.required();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (required) {
            return value != null && value >= min && value <= max;
        } else {
            return value == null || (value >= min && value <= max);
        }
    }
}
