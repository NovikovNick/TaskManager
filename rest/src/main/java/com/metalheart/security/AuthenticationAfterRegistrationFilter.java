package com.metalheart.security;

import com.metalheart.config.AppProperties;
import com.metalheart.integration.gateway.RegistrationGateway;
import com.metalheart.model.RegistrationResponse;
import com.metalheart.model.User;
import java.io.IOException;
import java.util.UUID;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
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
    private RegistrationGateway registrationGateway;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        String path = new UrlPathHelper().getPathWithinApplication(request);
        if(path.length() == 37 && path.substring(1).matches(UUID_REGEX)) {

            try {

                String registrationToken = path.substring(1);

                RegistrationResponse user = registrationGateway.confirmRegistration(UUID.fromString(registrationToken));

                if (user.getUser() == null) {

                    return;

                } else {

                    Token token = new Token(user.getUser());
                    token.setAuthenticated(true);
                    SecurityContextHolder.getContext().setAuthentication(token);

                    response.sendRedirect(properties.getRest().getFrontUrl());
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

    @Data
    public static class Token extends AbstractAuthenticationToken {

        private User principal;
        private Object credentials;

        public Token(User principal) {
            super(principal.getAuthorities());
            this.principal = principal;
        }
    }
}
