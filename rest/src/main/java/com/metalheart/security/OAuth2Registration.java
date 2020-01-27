package com.metalheart.security;

import com.metalheart.config.AppProperties;
import com.metalheart.model.User;
import com.metalheart.service.TaskService;
import com.metalheart.service.UserService;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class OAuth2Registration extends SimpleUrlAuthenticationSuccessHandler {


    @Autowired
    private AppProperties properties;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private TaskService taskService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        DefaultOidcUser oidcUser = (DefaultOidcUser) authentication.getPrincipal();

        String username = oidcUser.getAttribute("name");
        String email = oidcUser.getAttribute("email");

        User user = getUser(username, email);

        Token token = new Token(user);
        token.setAuthenticated(true);
        SecurityContextHolder.getContext().setAuthentication(token);

        taskService.reorder(user.getId());

        response.sendRedirect(properties.getRest().getFrontUrl());
    }

    private User getUser(String username, String email) {

        Optional<User> user = userService.findByUsername(username);

        if (user.isPresent()) {

            return user.get();

        } else {

            return userService.createUser(User.builder()
                .username(username)
                .email(email)
                .password(RandomStringUtils.random(8))
                .build());
        }
    }
}
