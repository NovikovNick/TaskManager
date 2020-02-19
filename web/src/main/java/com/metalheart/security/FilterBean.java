package com.metalheart.security;

import com.metalheart.service.UserService;
import java.io.IOException;
import javax.security.auth.Subject;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import static java.util.Arrays.asList;

//@Component
public class FilterBean extends GenericFilterBean {

    @Autowired
    private UserDetailsService userService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {


        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            SecurityContextHolder.getContext().setAuthentication(createAuthentication((HttpServletRequest) request));
        }
        doFilter(request, response, chain);
    }

    private Authentication createAuthentication(HttpServletRequest request) {

        DefaultOidcUser user = (DefaultOidcUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String username = user.getAttribute("name");
        UserDetails details = userService.loadUserByUsername(username);

        AbstractAuthenticationToken token =
            new AbstractAuthenticationToken(asList(new SimpleGrantedAuthority("USER_ROLE"))) {

            @Override
            public boolean implies(Subject subject) {
                return false;
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return getDetails();
            }
        };

        token.setDetails(details);
        return null;
    }
}
