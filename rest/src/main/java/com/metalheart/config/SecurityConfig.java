package com.metalheart.config;

import com.metalheart.security.OAuth2Registration;
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
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private OAuth2Registration oAuth2Registration;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

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
            .successHandler(oAuth2Registration)
            .and()
        .csrf()
            .disable()
        .exceptionHandling()
            .authenticationEntryPoint(new Http403ForbiddenEntryPoint())
            .and()
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
