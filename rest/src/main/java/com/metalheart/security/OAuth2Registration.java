package com.metalheart.security;

import com.metalheart.config.AppProperties;
import com.metalheart.model.User;
import com.metalheart.service.UserService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        DefaultOidcUser oidcUser = (DefaultOidcUser) authentication.getPrincipal();

        String username = oidcUser.getAttribute("name");
        String email = oidcUser.getAttribute("email");

        UserDetails user = getUserDetails(username, email);

        Token token = new Token(user);
        token.setAuthenticated(true);
        SecurityContextHolder.getContext().setAuthentication(token);

        response.sendRedirect(properties.getRest().getFrontUrl());
    }

    private UserDetails getUserDetails(String username, String email) {

        try {

            return userDetailsService.loadUserByUsername(username);

        } catch (UsernameNotFoundException e) {

            return userService.createUser(User.builder()
                .username(username)
                .email(email)
                .password(RandomStringUtils.random(8))
                .build());
        }
    }
}
