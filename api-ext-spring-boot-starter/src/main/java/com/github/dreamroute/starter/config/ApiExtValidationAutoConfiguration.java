/*
 * Copyright 2012-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.dreamroute.starter.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.validation.MessageInterpolatorFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Role;

import javax.validation.Validator;
import javax.validation.executable.ExecutableValidator;

/**
 * @author w.dehai
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(ExecutableValidator.class)
@ComponentScan("com.github.dreamroute.starter")
@ConditionalOnResource(resources = "classpath:META-INF/services/javax.validation.spi.ValidationProvider")
@Import(ApiExtPrimaryDefaultValidatorPostProcessor.class)
@AutoConfigureBefore(ValidationAutoConfiguration.class)
public class ApiExtValidationAutoConfiguration {

	@Bean
	@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
	@ConditionalOnMissingBean(Validator.class)
	public static ApiExtFactoryBean defaultValidator() {
		ApiExtFactoryBean factoryBean = new ApiExtFactoryBean();
		MessageInterpolatorFactory interpolatorFactory = new MessageInterpolatorFactory();
		factoryBean.setMessageInterpolator(interpolatorFactory.getObject());
		return factoryBean;
	}

	/* 如果spring boot版本是带有RELEASE的就会报错，如果是数字版本就不会报错，
	 * 下面的类只有数字版本才包含，比如2.3.7.RELEASE就不行，而2.4.0就可以
	 * 下方的对象启动时不创建似乎也不会报错**/
//	@Bean
//	@ConditionalOnMissingBean
//	public static MethodValidationPostProcessor methodValidationPostProcessor(Environment environment, @Lazy Validator validator, ObjectProvider<MethodValidationExcludeFilter> excludeFilters) {
//		FilteredMethodValidationPostProcessor processor = new FilteredMethodValidationPostProcessor(
//				excludeFilters.orderedStream());
//		boolean proxyTargetClass = environment.getProperty("spring.aop.proxy-target-class", Boolean.class, true);
//		processor.setProxyTargetClass(proxyTargetClass);
//		processor.setValidator(validator);
//		return processor;
//	}

}
