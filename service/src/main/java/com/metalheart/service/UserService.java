package com.metalheart.service;

import com.metalheart.model.User;
import java.util.Optional;

public interface UserService {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    User createUser(User user);

    boolean isUserExistByEmail(String email);

    User get(Integer id);

    void update(Integer userId, String newPassword);

    User update(User user);
}
