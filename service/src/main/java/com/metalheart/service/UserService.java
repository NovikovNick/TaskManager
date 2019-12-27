package com.metalheart.service;

public interface UserService {

    Integer createUser(String username, String email, String password);

    boolean isUserExistByEmail(String email);
}
