package com.metalheart.config;

import java.util.Set;
import javax.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

@Slf4j
@Configuration
@EnableScheduling
@ComponentScan(basePackages = {
    "com.metalheart.service",
    "com.metalheart.converter"
})
public class ServiceConfiguration {

    public static final String APP_CONVERSION_SERVICE = "APP_CONVERSION_SERVICE";

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

    @Autowired
    private Set<Converter> converters;

    @Bean
    @Qualifier(APP_CONVERSION_SERVICE)
    public ConversionService appConversionService() {
        ConversionServiceFactoryBean bean = new ConversionServiceFactoryBean();
        bean.setConverters(converters);
        bean.afterPropertiesSet();
        ConversionService object = bean.getObject();
        return object;
    }
}
