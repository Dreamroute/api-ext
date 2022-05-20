package com.github.dreamroute.starter.constraints.validator;

import com.github.dreamroute.starter.constraints.ApiExtArray;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Objects;

/**
 * 描述：数组校验器
 *
 * @author w.dehi.2022-05-17
 */
public class ApiExtArrayValidator implements ConstraintValidator<ApiExtArray, Object[]> {

    private boolean required;
    private boolean elementNotNull;

    @Override
    public void initialize(ApiExtArray anno) {
        required = anno.required();
        elementNotNull = anno.elementNotNull();
    }

    @Override
    public boolean isValid(Object[] value, ConstraintValidatorContext context) {
        if (required) {
            if (elementNotNull) {
                return value != null && value.length > 0 && Arrays.stream(value).noneMatch(Objects::isNull);
            } else {
                return value != null && value.length > 0;
            }
        }
        return true;
    }
}
