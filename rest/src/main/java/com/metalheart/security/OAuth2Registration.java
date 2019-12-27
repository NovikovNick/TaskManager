package com.metalheart.security;

import com.metalheart.config.AppRestProperties;
import com.metalheart.service.UserService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class OAuth2Registration extends SimpleUrlAuthenticationSuccessHandler {


    @Autowired
    private AppRestProperties properties;

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        DefaultOidcUser user = (DefaultOidcUser) authentication.getPrincipal();

        String username = user.getAttribute("name");
        String email = user.getAttribute("email");


        if (!userService.isUserExistByEmail(email)) {
            userService.createUser(username, email, RandomStringUtils.random(8));
        }


        response.sendRedirect(properties.getFrontUrl());
    }
}
