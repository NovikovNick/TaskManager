package com.metalheart;

import com.metalheart.config.RepositoryConfiguration;
import com.metalheart.config.ServiceConfiguration;
import com.metalheart.config.WebConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import( {
    WebConfiguration.class,
    ServiceConfiguration.class,
    RepositoryConfiguration.class
})
public class AppConfiguration {
}
