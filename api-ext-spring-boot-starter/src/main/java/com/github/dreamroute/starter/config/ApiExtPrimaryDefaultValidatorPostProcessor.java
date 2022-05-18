package com.github.dreamroute.starter.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * @author w.dehi.2022-05-17
 */
public class ApiExtPrimaryDefaultValidatorPostProcessor implements ImportBeanDefinitionRegistrar, BeanFactoryAware {

    /**
     * The bean name of the auto-configured Validator.
     */
    private static final String VALIDATOR_BEAN_NAME = "defaultValidator";

    private ConfigurableListableBeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        if (beanFactory instanceof ConfigurableListableBeanFactory) {
            this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
        }
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        BeanDefinition definition = getAutoConfiguredValidator(registry);
        if (definition != null) {
            definition.setPrimary(!hasPrimarySpringValidator());
        }
    }

    private BeanDefinition getAutoConfiguredValidator(BeanDefinitionRegistry registry) {
        if (registry.containsBeanDefinition(VALIDATOR_BEAN_NAME)) {
            BeanDefinition definition = registry.getBeanDefinition(VALIDATOR_BEAN_NAME);
            if (definition.getRole() == BeanDefinition.ROLE_INFRASTRUCTURE
                    && isTypeMatch(VALIDATOR_BEAN_NAME, LocalValidatorFactoryBean.class)) {
                return definition;
            }
        }
        return null;
    }

    private boolean isTypeMatch(String name, Class<?> type) {
        return this.beanFactory != null && this.beanFactory.isTypeMatch(name, type);
    }

    private boolean hasPrimarySpringValidator() {
        String[] validatorBeans = this.beanFactory.getBeanNamesForType(Validator.class, false, false);
        for (String validatorBean : validatorBeans) {
            BeanDefinition definition = this.beanFactory.getBeanDefinition(validatorBean);
            if (definition.isPrimary()) {
                return true;
            }
        }
        return false;
    }

}
