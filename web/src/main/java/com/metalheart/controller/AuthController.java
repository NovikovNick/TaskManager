package com.metalheart.controller;

import com.metalheart.EndPoint;
import com.metalheart.model.RunningList;
import com.metalheart.model.User;
import com.metalheart.model.request.AuthenticationRequest;
import com.metalheart.model.response.LoginResponseViewModel;
import com.metalheart.model.response.RunningListViewModel;
import com.metalheart.model.response.UserViewModel;
import com.metalheart.service.AuthService;
import com.metalheart.service.RunningListArchiveService;
import com.metalheart.service.RunningListService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
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
import static com.metalheart.config.ServiceConfiguration.APP_CONVERSION_SERVICE;

@Slf4j
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private RunningListService runningListService;

    @Autowired
    private RunningListArchiveService archiveService;

    @Autowired
    @Qualifier(APP_CONVERSION_SERVICE)
    private ConversionService conversionService;

    @PostMapping(EndPoint.AUTH_SIGN_IN)
    public LoginResponseViewModel signin(
        @RequestHeader(HEADER_TIMEZONE_OFFSET) Integer timezoneOffset,
        @RequestBody @Valid AuthenticationRequest authenticationRequest,
        HttpServletRequest httpServletRequest) throws AuthenticationException {

        User user = authService.authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final HttpSession session = httpServletRequest.getSession(true);
        session.setAttribute(
            HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
            SecurityContextHolder.getContext());

        return geRunningListDataViewModel(user, timezoneOffset);
    }

    @GetMapping(EndPoint.RUNNING_LIST_DATA)
    public LoginResponseViewModel getRunningListData(@RequestHeader(HEADER_TIMEZONE_OFFSET) Integer timezoneOffset,
                                                     @AuthenticationPrincipal User user) throws AuthenticationException {

        return geRunningListDataViewModel(user, timezoneOffset);
    }

    private LoginResponseViewModel geRunningListDataViewModel(User user, Integer timezoneOffset) {

        RunningList runningList = runningListService.getRunningList(user.getId(), timezoneOffset);

        LoginResponseViewModel res = LoginResponseViewModel.builder()
            .user(conversionService.convert(user, UserViewModel.class))
            .runningList(conversionService.convert(runningList, RunningListViewModel.class))
            .archives(archiveService.getExistingArchivesWeekIds(user.getId()))
            .build();
        return res;
    }
}
