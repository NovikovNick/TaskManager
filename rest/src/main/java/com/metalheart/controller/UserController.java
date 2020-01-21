package com.metalheart.controller;

import com.metalheart.integration.gateway.RegistrationGateway;
import com.metalheart.model.User;
import com.metalheart.model.request.UserRegistrationRequest;
import com.metalheart.model.response.UserViewModel;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.metalheart.EndPoint.USER_REGISTRATION;
import static com.metalheart.config.ServiceConfiguration.APP_CONVERSION_SERVICE;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    @Autowired
    @Qualifier(APP_CONVERSION_SERVICE)
    private ConversionService conversionService;

    @Autowired
    private RegistrationGateway registrationGateway;

    @GetMapping
    public UserViewModel getUser(@AuthenticationPrincipal Object user) {
        return conversionService.convert(user, UserViewModel.class);
    }

    @PostMapping(USER_REGISTRATION)
    public ResponseEntity createUser(@RequestBody @Valid UserRegistrationRequest request) {

        User userToCreate = conversionService.convert(request, User.class);
        registrationGateway.startRegistration(userToCreate);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
