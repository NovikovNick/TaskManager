package com.metalheart.config;

import com.metalheart.rest.LogRequestResponseFilter;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Import(SwaggerConfiguration.class)
@EnableWebMvc
@ComponentScan("com.metalheart.controller")
public class RestConfiguration implements WebMvcConfigurer {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private Collection<Converter> converters;

    @Bean
    public MessageSourceAccessor messageSourceAccessor() {
        return new MessageSourceAccessor(messageSource);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("**/swagger-ui.html")
            .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler(
            "/webjars/**",
            "/images/**",
            "/css/**",
            "/js/**")
            .addResourceLocations(
                "classpath:/META-INF/resources/webjars/",
                "classpath:/static/images/",
                "classpath:/static/css/",
                "classpath:/static/js/");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        converters.forEach(registry::addConverter);
    }

    @Bean
    public LogRequestResponseFilter getLogRequestResponseFilter() {
        return LogRequestResponseFilter.builder()
            .maxPayloadLength(10000)
            .includeRequest(true)
            .includeRequestHeaders(false)
            .includeResponse(false)
            .includeResponseHeaders(false)
            .build();
    }
}
