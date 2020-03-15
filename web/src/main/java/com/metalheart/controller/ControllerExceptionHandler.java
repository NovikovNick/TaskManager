package com.metalheart.controller;

import com.google.common.collect.Lists;
import com.metalheart.model.response.FormValidationErrorViewModel;
import com.metalheart.model.response.Response;
import com.metalheart.service.WebService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.metalheart.HTTPConstants.MSG_ERROR_BADREQUEST;
import static com.metalheart.HTTPConstants.MSG_ERROR_FORBIDDEN;
import static java.util.Arrays.asList;

@Slf4j
@RestControllerAdvice
@Component
public class ControllerExceptionHandler {

    @Autowired
    private WebService webService;

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, List<String>> handle(MethodArgumentNotValidException exception,
                                            HttpServletRequest httpRequest) {

        final BindingResult bindingResult = exception.getBindingResult();
        final List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        final List<FormValidationErrorViewModel.ParameterValidationError> errors = Lists.newArrayList();

        Map<String, List<String>> res = new HashMap<>();

        for (final FieldError fieldError : fieldErrors) {
            res.put(fieldError.getField(), asList(fieldError.getDefaultMessage()));
        }

        return res;
    }

    @ExceptionHandler
    @ResponseBody
    public ResponseEntity<Response> handle(BadCredentialsException exception, HttpServletRequest httpRequest) {

        return webService.getResponseBuilder()
            .status(HttpStatus.FORBIDDEN)
            .error(MSG_ERROR_FORBIDDEN, httpRequest.getLocale())
            .build();
    }


    @ExceptionHandler
    @ResponseBody
    public ResponseEntity<Response> handle(Exception exception, HttpServletRequest httpRequest) {

        return webService.getResponseBuilder()
            .status(HttpStatus.BAD_REQUEST)
            .error(MSG_ERROR_BADREQUEST, httpRequest.getLocale())
            .build();
    }
}
