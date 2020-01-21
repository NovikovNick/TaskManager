package com.metalheart.service;

import com.metalheart.model.User;

public interface UserService {

    User createUser(User user);

    boolean isUserExistByEmail(String email);

    User get(Integer id);
}
