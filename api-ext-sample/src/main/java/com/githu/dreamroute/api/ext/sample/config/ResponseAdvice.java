package com.githu.dreamroute.api.ext.sample.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

import static java.util.stream.Collectors.joining;

/**
 * 描述：// TODO 此处必填
 *
 * @author w.dehi.2022-05-19
 */
@Slf4j
@RestControllerAdvice
public class ResponseAdvice {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object validateException(MethodArgumentNotValidException e) {
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        return allErrors.stream().map(ObjectError::getDefaultMessage).distinct().collect(joining(", "));
    }
}
