package com.metalheart.service.impl;

import com.metalheart.log.LogContextField;
import com.metalheart.log.LogOperationContext;
import com.metalheart.model.User;
import com.metalheart.service.AuthService;
import com.metalheart.service.TaskService;
import com.metalheart.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @LogOperationContext
    @Override
    public User authenticate(String username, String password) {

        var token = new UsernamePasswordAuthenticationToken(username, password);

        Authentication authentication = authenticationManager.authenticate(token);

        // Inject into security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userService.findByUsername(username).get();

        taskService.reorder(user.getId());
        userService.updateLastLogin(user.getId());

        return user;
    }
}
