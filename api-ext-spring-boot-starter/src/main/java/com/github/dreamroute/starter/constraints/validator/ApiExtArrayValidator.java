package com.github.dreamroute.starter.constraints.validator;

import com.github.dreamroute.starter.constraints.ApiExtArray;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 描述：数组校验器
 *
 * @author w.dehi.2022-05-17
 */
public class ApiExtArrayValidator implements ConstraintValidator<ApiExtArray, Object[]> {

    private boolean required;
    private int min;
    private int max;

    @Override
    public void initialize(ApiExtArray anno) {
        required = anno.required();
        min = anno.min();
        max = anno.max();
    }

    @Override
    public boolean isValid(Object[] value, ConstraintValidatorContext context) {
        if (required) {
            return value != null && value.length >= min && value.length <= max;
        } else {
            // required = false时， min无效
            return value == null || value.length == 0 || value.length <= max;
        }
    }
}
