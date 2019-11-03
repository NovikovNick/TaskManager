package com.metalheart;

import com.metalheart.config.RepositoryConfiguration;
import com.metalheart.config.RestConfiguration;
import com.metalheart.config.ServiceConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
    RestConfiguration.class,
    ServiceConfiguration.class,
    RepositoryConfiguration.class
})
public class AppConfiguration {
}
