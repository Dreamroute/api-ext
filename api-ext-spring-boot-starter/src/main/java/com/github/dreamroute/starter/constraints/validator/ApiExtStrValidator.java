package com.github.dreamroute.starter.constraints.validator;

import com.github.dreamroute.starter.constraints.ApiExtStr;
import org.springframework.core.annotation.AnnotationUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Map;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * 描述：{@link String}校验器
 *
 * @author w.dehi.2022-05-17
 */
public class ApiExtStrValidator implements ConstraintValidator<ApiExtStr, String> {

    private boolean required;
    private int min;
    private int max;

    @Override
    public void initialize(ApiExtStr anno) {
        Map<String, Object> attrs = AnnotationUtils.getAnnotationAttributes(anno);
        max = (int) attrs.get("max");
        min = (int) attrs.get("min");
        required = (boolean) AnnotationUtils.getValue(anno, "required");
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (required) {
            return !isEmpty(value) && value.length() >= min && value.length() <= max;
        } else {
            return isEmpty(value) || (value.length() >= min && value.length() <= max);
        }
    }
}
