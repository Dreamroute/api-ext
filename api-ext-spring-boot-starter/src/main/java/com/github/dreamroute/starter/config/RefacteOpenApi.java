package com.github.dreamroute.starter.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.util.ArrayList;
import java.util.Objects;

import static java.util.Optional.ofNullable;

/**
 * @author w.dehi.2022-05-23
 */
@Slf4j
@Aspect
public class RefacteOpenApi {

    @Around("execution(public * springfox.documentation.oas.web.WebMvcBasePathAndHostnameTransformationFilter.transform(..))")
    public Object process(ProceedingJoinPoint point) throws Throwable {
        Object[] args = point.getArgs();
        Object result = point.proceed(args);
        if (result instanceof OpenAPI) {
            OpenAPI resp = (OpenAPI) result;
            resp.setInfo(null);
            ofNullable(resp.getTags()).orElseGet(ArrayList::new).removeIf(e -> Objects.equals("basic-error-controller", e.getName()));
            Paths paths = resp.getPaths();
            paths.remove("/error");
            return resp;
        }
        return result;
    }
}