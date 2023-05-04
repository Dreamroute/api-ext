package com.github.dreamroute.starter.constraints.validator;

import com.github.dreamroute.starter.constraints.ApiExtEnum;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 描述：枚举校验器
 *
 * @author w.dehi.2023-05-04
 */
public class ApiExtEnumValidator implements ConstraintValidator<ApiExtEnum, Object> {

    private boolean required;

    @Override
    public void initialize(ApiExtEnum anno) {
        required = anno.required();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (required) {
            return value != null;
        }
        return true;
    }

}
