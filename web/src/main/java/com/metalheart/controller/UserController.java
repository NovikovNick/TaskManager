package com.metalheart.controller;

import com.metalheart.model.Tag;
import com.metalheart.model.User;
import com.metalheart.model.request.StartChangePasswordPasswordRequest;
import com.metalheart.model.request.UpdatePasswordRequest;
import com.metalheart.model.request.UpdateProfileRequest;
import com.metalheart.model.request.UserRegistrationRequest;
import com.metalheart.model.response.RunningListDataViewModel;
import com.metalheart.model.response.UserViewModel;
import com.metalheart.service.ChangePasswordService;
import com.metalheart.service.RegistrationService;
import com.metalheart.service.RunningListCommandService;
import com.metalheart.service.RunningListService;
import com.metalheart.service.UserService;
import com.metalheart.service.WebService;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.metalheart.EndPoint.CHANGE_PASSWORD;
import static com.metalheart.EndPoint.SAVE_PROFILE;
import static com.metalheart.EndPoint.SEND_CHANGE_PASSWORD_EMAIL;
import static com.metalheart.EndPoint.USER_REGISTRATION;
import static com.metalheart.HTTPConstants.HEADER_TIMEZONE_OFFSET;
import static com.metalheart.config.ServiceConfiguration.APP_CONVERSION_SERVICE;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    @Autowired
    @Qualifier(APP_CONVERSION_SERVICE)
    private ConversionService conversionService;

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private ChangePasswordService changePasswordService;

    @Autowired
    private RunningListCommandService commandService;

    @Autowired
    private RunningListService runningListService;

    @Autowired
    private UserService userService;

    @Autowired
    private WebService webService;


    @GetMapping(USER_REGISTRATION)
    public UserViewModel getUser(@AuthenticationPrincipal User user) {
        return conversionService.convert(userService.get(user.getId()), UserViewModel.class);
    }

    @PostMapping(USER_REGISTRATION)
    public ResponseEntity createUser(@RequestBody @Valid UserRegistrationRequest request) {

        User userToCreate = conversionService.convert(request, User.class);
        boolean started = registrationService.startRegistration(userToCreate);

        return ResponseEntity.status(started ? HttpStatus.OK : HttpStatus.BAD_GATEWAY).body(new HashMap<>());
    }

    @PostMapping(SAVE_PROFILE)
    public RunningListDataViewModel updateProfile(@RequestHeader(HEADER_TIMEZONE_OFFSET) Integer timezoneOffset,
                                                  @AuthenticationPrincipal User user,
                                                  @RequestBody @Valid UpdateProfileRequest request) {

        List<Tag> tags = request.getTags().stream()
            .map(tag -> conversionService.convert(tag, Tag.class))
            .collect(Collectors.toList());

        commandService.updateProfile(user.getId(), request.getUsername(), request.getEmail(), tags);

        RunningListDataViewModel data = webService.geRunningListDataViewModel(user, timezoneOffset);
        return data;
    }

    @PostMapping(SEND_CHANGE_PASSWORD_EMAIL)
    public ResponseEntity startResetPassword(@RequestBody @Valid StartChangePasswordPasswordRequest request) {

        boolean started = changePasswordService.startChangePasswordProcessing(request.getEmail());

        return ResponseEntity
            .status(started ? HttpStatus.OK : HttpStatus.BAD_GATEWAY)
            .body(new HashMap<>());
    }

    @PostMapping(CHANGE_PASSWORD)
    public ResponseEntity changePassword(@AuthenticationPrincipal User user,
                                         @RequestBody @Valid UpdatePasswordRequest request) {

        userService.update(user.getId(), request.getPassword());
        return ResponseEntity.status(HttpStatus.OK).body(new HashMap<>());
    }
}
