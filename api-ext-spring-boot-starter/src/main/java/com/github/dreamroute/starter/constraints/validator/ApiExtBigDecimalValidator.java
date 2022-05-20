package com.github.dreamroute.starter.constraints.validator;

import com.github.dreamroute.starter.constraints.ApiExtBigDecimal;
import org.springframework.core.annotation.AnnotationUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 描述：{@link Integer}校验器
 *
 * @author w.dehi.2022-05-17
 */
public class ApiExtBigDecimalValidator implements ConstraintValidator<ApiExtBigDecimal, BigDecimal> {

    private boolean required;
    private BigDecimal min;
    private BigDecimal max;

    @Override
    public void initialize(ApiExtBigDecimal anno) {
        Map<String, Object> attrs = AnnotationUtils.getAnnotationAttributes(anno);
        max = new BigDecimal( String.valueOf(attrs.get("max")));
        min = new BigDecimal(String.valueOf(attrs.get("min")));
        required = anno.required();
    }

    @Override
    public boolean isValid(BigDecimal value, ConstraintValidatorContext context) {
        if (required) {
            return value != null && value.compareTo(min) >= 0 && value.compareTo(max) <= 0;
        } else {
            return value == null || (value.compareTo(min) >= 0 && value.compareTo(max) <= 0);
        }
    }

}
