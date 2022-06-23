package com.github.dreamroute.starter.constraints.validator;

import cn.hutool.core.date.DateUtil;
import com.github.dreamroute.starter.constraints.ApiExtDate;
import com.github.dreamroute.starter.constraints.ApiExtDate.Phase;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Date;

import static com.github.dreamroute.starter.constraints.ApiExtDate.Phase.All;

/**
 * 描述：日期校验器
 *
 * @author w.dehi.2022-05-17
 */
public class ApiExtDateValidator implements ConstraintValidator<ApiExtDate, Date> {

    private boolean required;
    private Phase phase;

    @Override
    public void initialize(ApiExtDate anno) {
        required = anno.required();
        phase = anno.phase();
    }

    @Override
    public boolean isValid(Date value, ConstraintValidatorContext context) {
        int result = DateUtil.compare(value, new Date());
        boolean past = result < 0;
        boolean pastOrPresent = result <= 0;
        boolean future = result > 0;
        boolean futureOrPresent = result >= 0;

        if (required) {
            if (phase == All) {
                return value != null;
            } else {
                if (value == null) {
                    return false;
                }
                return deduce(past, pastOrPresent, future, futureOrPresent);
            }
        } else {
            if (value == null) {
                return true;
            }
            Date now = new Date();
            return deduce(past, pastOrPresent, future, futureOrPresent);
        }
    }

    private boolean deduce(boolean past, boolean pastOrPresent, boolean future, boolean futureOrPresent) {
        switch (phase) {
            case Past: {
                return past;
            }
            case PastOrPresent: {
                return pastOrPresent;
            }
            case Future: {
                return future;
            }
            case FutureOrPresent: {
                return futureOrPresent;
            }
            default:
                return true;
        }
    }
}
