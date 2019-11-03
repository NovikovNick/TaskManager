package com.metalheart.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@Configuration
@EntityScan(basePackages = "com.metalheart.model")
@EnableJpaRepositories(value = "com.metalheart.repository.jpa")
@ComponentScan("com.metalheart.repository")
@EnableTransactionManagement
public class RepositoryConfiguration {
}
