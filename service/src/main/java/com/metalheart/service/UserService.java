package com.metalheart.service;

import com.metalheart.model.User;
import java.util.Optional;

public interface UserService {

    Optional<User> findByUsername(String username);

    User createUser(User user);

    boolean isUserExistByEmail(String email);

    User get(Integer id);
}
