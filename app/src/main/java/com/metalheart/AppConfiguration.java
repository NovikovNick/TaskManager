package com.metalheart;

import com.metalheart.config.AppProperties;
import com.metalheart.config.RepositoryConfiguration;
import com.metalheart.config.RestConfiguration;
import com.metalheart.config.ServiceConfiguration;
import com.metalheart.model.User;
import com.metalheart.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
    RestConfiguration.class,
    ServiceConfiguration.class,
    RepositoryConfiguration.class
})
public class AppConfiguration {

    @Autowired
    private AppProperties properties;

    @Autowired
    private UserService userService;

    @Bean
    public ApplicationRunner applicationRunner() {
        return arg -> {

            if(!userService.isUserExistByEmail(properties.getSecurity().getDefaultEmail())) {

                userService.createUser(User.builder()
                    .email(properties.getSecurity().getDefaultEmail())
                    .username(properties.getSecurity().getDefaultUsername())
                    .password(properties.getSecurity().getDefaultPassword())
                    .build());
            }
        };
    }
}
