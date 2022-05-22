package com.github.dreamroute.starter.constraints.validator;

import com.github.dreamroute.starter.constraints.ApiExtCollection;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collection;

/**
 * 描述：集合校验器
 *
 * @author w.dehi.2022-05-17
 */
public class ApiExtCollectionValidator implements ConstraintValidator<ApiExtCollection, Collection<?>> {

    private boolean required;
    private int min;
    private int max;

    @Override
    public void initialize(ApiExtCollection anno) {
        required = anno.required();
        min = anno.min();
        max = anno.max();
    }

    @Override
    public boolean isValid(Collection<?> value, ConstraintValidatorContext context) {
        if (required) {
            return value != null && value.size() >= min && value.size() <= max;
        } else {
            // required = false时， min无效
            return value == null || value.isEmpty() || value.size() <= max;
        }
    }
}