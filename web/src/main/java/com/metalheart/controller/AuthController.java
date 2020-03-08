package com.metalheart.controller;

import com.metalheart.EndPoint;
import com.metalheart.model.User;
import com.metalheart.model.request.AuthenticationRequest;
import com.metalheart.model.response.RunningListDataViewModel;
import com.metalheart.service.AuthService;
import com.metalheart.service.WebService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.metalheart.HTTPConstants.HEADER_TIMEZONE_OFFSET;

@Slf4j
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private WebService webService;

    @PostMapping(EndPoint.AUTH_SIGN_IN)
    public RunningListDataViewModel signin(@RequestHeader(HEADER_TIMEZONE_OFFSET) Integer timezoneOffset,
                                           @RequestBody @Valid AuthenticationRequest authenticationRequest,
                                           HttpServletRequest httpServletRequest) throws AuthenticationException {

        User user = authService.authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final HttpSession session = httpServletRequest.getSession(true);
        session.setAttribute(
            HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
            SecurityContextHolder.getContext());

        return webService.geRunningListDataViewModel(user, timezoneOffset);
    }

    @GetMapping(EndPoint.RUNNING_LIST_DATA)
    public RunningListDataViewModel getRunningListData(@RequestHeader(HEADER_TIMEZONE_OFFSET) Integer timezoneOffset,
                                                       @AuthenticationPrincipal User user) throws AuthenticationException {

        return webService.geRunningListDataViewModel(user, timezoneOffset);
    }
}
