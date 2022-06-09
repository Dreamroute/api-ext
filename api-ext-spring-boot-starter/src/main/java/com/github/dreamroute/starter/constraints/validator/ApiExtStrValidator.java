package com.github.dreamroute.starter.constraints.validator;

import com.github.dreamroute.starter.constraints.ApiExtStr;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * 描述：{@link String}校验器
 *
 * @author w.dehi.2022-05-17
 */
public class ApiExtStrValidator implements ConstraintValidator<ApiExtStr, String> {

    private boolean required;
    private int max;
    private int min;

    @Override
    public void initialize(ApiExtStr anno) {
        max = anno.max();
        min = anno.min();
        required = anno.required();
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
