package com.github.dreamroute.starter.constraints.validator;

import com.github.dreamroute.starter.constraints.ApiExtInteger;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

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
        max = anno.max();
        min = anno.min();
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
