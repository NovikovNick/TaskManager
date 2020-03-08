package com.metalheart.service;

import com.metalheart.model.User;

public interface AuthService {

    User authenticate(String username, String password);
}
