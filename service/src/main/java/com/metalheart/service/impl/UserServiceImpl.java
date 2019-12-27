package com.metalheart.service.impl;

import com.metalheart.model.User;
import com.metalheart.model.jpa.UserJpa;
import com.metalheart.repository.jpa.UserJpaRepository;
import com.metalheart.service.UserService;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserJpaRepository repository;

    @Autowired
    private ConversionService conversionService;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<UserJpa> userOpt = repository.findByUsername(username);

        if (userOpt.isPresent()) {
            return conversionService.convert(userOpt.get(), User.class);
        } else {
            throw new UsernameNotFoundException("There is no such user with username: " + username);
        }
    }

    @Override
    public Integer createUser(String username, String email, String password) {

        UserJpa res = repository.save(UserJpa.builder()
            .username(username)
            .email(email)
            .password(encoder.encode(password))
            .build());

        log.info("User has been created");
        return res.getId();
    }

    @Override
    public boolean isUserExistByEmail(String email) {
        return repository.existsByEmail(email);
    }
}
