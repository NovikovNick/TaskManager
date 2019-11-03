package com.metalheart.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Import(SwaggerConfiguration.class)
@EnableWebMvc
@ComponentScan("com.metalheart.rest")
public class RestConfiguration implements WebMvcConfigurer {

    @Autowired
    private MessageSource messageSource;

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
            "/images/**",
            "/css/**",
            "/js/**")
            .addResourceLocations(
                "classpath:/META-INF/resources/webjars/",
                "classpath:/static/images/",
                "classpath:/static/css/",
                "classpath:/static/js/");
    }
}
