package com.githu.dreamroute.api.ext.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author w.dehi.2022-05-18
 */
@EnableAspectJAutoProxy
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class ApiExtApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiExtApplication.class, args);
    }
}
