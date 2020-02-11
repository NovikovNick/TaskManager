package com.metalheart.service.impl;

import com.metalheart.model.User;
import com.metalheart.model.jpa.UserJpa;
import com.metalheart.repository.jpa.UserJpaRepository;
import com.metalheart.service.UserService;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.metalheart.config.ServiceConfiguration.APP_CONVERSION_SERVICE;

@Slf4j
@Component
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserJpaRepository repository;

    @Autowired
    @Qualifier(APP_CONVERSION_SERVICE)
    private ConversionService conversionService;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("There is no such user with username: " + username));
        return user;
    }

    @Override
    public Optional<User> findByUsername(String username) {

        Optional<UserJpa> user = repository.findByUsername(username);
        if (user.isPresent()) {
            return Optional.of(conversionService.convert(user.get(), User.class));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {

        Optional<UserJpa> user = repository.findByEmail(email);
        if (user.isPresent()) {
            return Optional.of(conversionService.convert(user.get(), User.class));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public User createUser(User user) {

        UserJpa res = repository.save(UserJpa.builder()
            .username(user.getUsername())
            .email(user.getEmail())
            .password(encoder.encode(user.getPassword()))
            .build());

        log.info("User has been created");

        return conversionService.convert(res, User.class);
    }

    @Override
    public boolean isUserExistByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Transactional
    @Override
    public User get(Integer id) {
        return conversionService.convert(repository.getOne(id), User.class);
    }

    @Transactional
    @Override
    public void update(Integer userId, String newPassword) {
        repository.updatePassword(userId, encoder.encode(newPassword));
    }

    @Override
    public User update(User user) {
        UserJpa userJpa = conversionService.convert(user, UserJpa.class);
        userJpa = repository.save(userJpa);
        return conversionService.convert(userJpa, User.class);
    }
}
