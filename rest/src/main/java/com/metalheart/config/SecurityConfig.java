package com.metalheart.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AppRestProperties properties;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        AuthenticationSuccessHandler handler;
        http.authorizeRequests()
            .antMatchers(
                "/js/**",
                "/css/**",
                "/images/**",
                "/webjars/**").permitAll()
            .antMatchers("/taskmanager/**").authenticated()
            .antMatchers("/auth/signin/**").permitAll()
            .anyRequest().authenticated()
            .and()

        .oauth2Login()
            .defaultSuccessUrl(properties.getFrontUrl(), true)
            .and()
        .csrf()
            .disable()
        .exceptionHandling()
            .authenticationEntryPoint(new Http403ForbiddenEntryPoint())
        ;
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsService userService) {
        final DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(encoder());
        return authProvider;
    }

    @Autowired
    public void initialize(AuthenticationManagerBuilder auth,
                           UserDetailsService userService,
                           DataSource dataSource) throws Exception {

        auth.userDetailsService(userService)
            .passwordEncoder(encoder())
            .and()
            .authenticationProvider(authenticationProvider(userService))
            .jdbcAuthentication()
            .usersByUsernameQuery("SELECT username, password, true FROM \"user\" WHERE username=?")
            .authoritiesByUsernameQuery("SELECT username, 'ROLE_USER' FROM \"user\" WHERE username=?")
            .dataSource(dataSource)
        ;
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
