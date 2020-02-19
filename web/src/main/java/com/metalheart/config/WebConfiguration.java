package com.metalheart.config;

import com.metalheart.rest.LogRequestResponseFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Import(SwaggerConfiguration.class)
@EnableWebMvc
@ComponentScan( {
    "com.metalheart.controller",
    "com.metalheart.filter"
})
public class WebConfiguration implements WebMvcConfigurer {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private AppProperties appProperties;

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
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH");
    }

    @Bean
    public LogRequestResponseFilter getLogRequestResponseFilter() {
        return LogRequestResponseFilter.builder()
            .maxPayloadLength(appProperties.getRest().getMaxPayloadLength())
            .includeRequest(appProperties.getRest().isIncludeRequest())
            .includeRequestHeaders(appProperties.getRest().isIncludeRequestHeaders())
            .includeResponse(appProperties.getRest().isIncludeResponse())
            .includeResponseHeaders(appProperties.getRest().isIncludeResponseHeaders())
            .build();
    }
}
