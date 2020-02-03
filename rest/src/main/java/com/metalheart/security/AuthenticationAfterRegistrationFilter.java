package com.metalheart.security;

import com.metalheart.config.AppProperties;
import com.metalheart.model.User;
import com.metalheart.service.RegistrationService;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UrlPathHelper;

@Slf4j
@Component
public class AuthenticationAfterRegistrationFilter extends OncePerRequestFilter {

    public static final String UUID_REGEX = "[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}";

    @Autowired
    private AppProperties properties;

    @Autowired
    private RegistrationService registrationService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        String path = new UrlPathHelper().getPathWithinApplication(request);
        if(path.length() == 37 && path.substring(1).matches(UUID_REGEX)) {

            try {

                String registrationToken = path.substring(1);

                Optional<User> user =
                    registrationService.confirmRegistration(UUID.fromString(registrationToken));

                if (user.isPresent()) {

                    Token token = new Token(user.get());
                    token.setAuthenticated(true);
                    SecurityContextHolder.getContext().setAuthentication(token);

                    response.sendRedirect(properties.getRest().getFrontUrl());
                    return;

                } else {
                    return;
                }

            } catch (Exception failed) {
                log.error(failed.getMessage(), failed);
                SecurityContextHolder.clearContext();
                chain.doFilter(request, response);
            }

        }

        chain.doFilter(request, response);
    }
}
