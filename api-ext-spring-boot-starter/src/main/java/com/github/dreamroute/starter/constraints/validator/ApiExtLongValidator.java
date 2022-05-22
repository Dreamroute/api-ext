package com.github.dreamroute.starter.constraints.validator;

import com.github.dreamroute.starter.constraints.ApiExtLong;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 描述：{@link Long}校验器
 *
 * @author w.dehi.2022-05-17
 */
public class ApiExtLongValidator implements ConstraintValidator<ApiExtLong, Long> {

    private boolean required;
    private Long min;
    private Long max;

    @Override
    public void initialize(ApiExtLong anno) {
        max = anno.max();
        min = anno.min();
        required = anno.required();
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        if (required) {
            return value != null && value >= min && value <= max;
        } else {
            return value == null || (value >= min && value <= max);
        }
    }
}
