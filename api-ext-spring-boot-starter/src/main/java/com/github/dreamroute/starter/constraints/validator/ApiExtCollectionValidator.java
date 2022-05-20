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

    @Override
    public void initialize(ApiExtCollection anno) {
        required = anno.required();
    }

    @Override
    public boolean isValid(Collection<?> value, ConstraintValidatorContext context) {
        if (required) {
            return value != null && !value.isEmpty();
        }
        return true;
    }
}
