package com.metalheart.test.integration.config;

import java.sql.SQLException;
import java.util.Properties;
import javax.sql.DataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class DataSourceTestConfiguration {

    @Bean
    @Primary
    public DataSource getDataSource() throws SQLException {
        return DataSourceBuilder.create()
            .url(System.getProperty("DB_URL"))
            .username(System.getProperty("DB_USERNAME"))
            .password(System.getProperty("DB_PASSWORD"))
            .driverClassName(System.getProperty("DB_DRIVER"))
            .build();
    }

    @Bean
    @Primary
    public ObjectMapper getObjectMapper(){
        return new ObjectMapper();
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) throws SQLException {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setDataSource(dataSource);

        entityManagerFactory.setPackagesToScan("com.metalheart.model.jpa");

        entityManagerFactory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        Properties properties = new Properties();
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQL95Dialect");
        properties.put("hibernate.naming-strategy", "org.hibernate.cfg.ImprovedNamingStrategy");
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.format_sql", "true");
        entityManagerFactory.setJpaProperties(properties);

        return entityManagerFactory;
    }
}
