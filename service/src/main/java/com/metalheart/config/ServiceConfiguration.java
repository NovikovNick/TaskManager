package com.metalheart.config;

import com.metalheart.service.TaskService;
import javax.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

@Slf4j
@Configuration
@ComponentScan(basePackages = {
    "com.metalheart.service",
    "com.metalheart.converter"
})
public class ServiceConfiguration {

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor(Validator validator) {
        MethodValidationPostProcessor methodValidationPostProcessor = new MethodValidationPostProcessor();
        methodValidationPostProcessor.setValidator(validator());
        return methodValidationPostProcessor;
    }

    @Bean
    public Validator validator() {
        LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
        return factoryBean;
    }

    @Bean
    public ApplicationRunner applicationRunner(TaskService taskService) {
        return arg -> taskService.reorder();
    }
}
