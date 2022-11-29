package com.github.dreamroute.starter.constraints.validator;

import com.github.dreamroute.starter.constraints.ApiExtBoolean;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

/**
 * 描述：{@link Boolean}校验器
 *
 * @author w.dehi.2022-11-28
 */
public class ApiExtBooleanValidator implements ConstraintValidator<ApiExtBoolean, Boolean> {

    private boolean required;

    @Override
    public void initialize(ApiExtBoolean anno) {
        required = anno.required();
    }

    @Override
    public boolean isValid(Boolean value, ConstraintValidatorContext context) {
        if (required) {
            return (TRUE == value || FALSE == value);
        } else {
            return value == null || TRUE == value || FALSE == value;
        }
    }
}