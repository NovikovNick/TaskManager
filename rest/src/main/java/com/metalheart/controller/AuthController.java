package com.metalheart.controller;

import com.metalheart.EndPoint;
import com.metalheart.model.request.AuthenticationRequest;
import com.metalheart.service.AuthService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    @Autowired
    private AuthService authService;

    @RequestMapping(value = EndPoint.AUTH_SIGN_IN, method = RequestMethod.POST)
    public AuthenticationResult signin(
        @RequestBody @Valid AuthenticationRequest authenticationRequest,
        HttpServletRequest httpServletRequest) throws AuthenticationException {

        authService.authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final HttpSession session = httpServletRequest.getSession(true);
        session.setAttribute(
            HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
            SecurityContextHolder.getContext());

        return AuthenticationResult.builder()
            .username(authenticationRequest.getUsername())
            .token(session.getId())
            .build();
    }

    @Data
    @Builder
    public static class AuthenticationResult {
        private String username;
        private String token;
    }
}
