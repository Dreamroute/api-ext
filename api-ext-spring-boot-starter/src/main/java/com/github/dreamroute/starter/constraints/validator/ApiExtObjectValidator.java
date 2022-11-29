package com.github.dreamroute.starter.constraints.validator;

import com.github.dreamroute.starter.constraints.ApiExtObject;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 描述：{@link Object}校验器
 *
 * @author w.dehi.2022-05-17
 */
public class ApiExtObjectValidator implements ConstraintValidator<ApiExtObject, Object> {

    private boolean required;

    @Override
    public void initialize(ApiExtObject anno) {
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
