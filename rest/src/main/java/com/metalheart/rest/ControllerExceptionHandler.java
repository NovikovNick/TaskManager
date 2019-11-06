package com.metalheart.rest;

import com.google.common.collect.Lists;
import com.metalheart.model.FormValidationErrorViewModel;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static java.util.Arrays.asList;

@Slf4j
@RestControllerAdvice
@Component
public class ControllerExceptionHandler {

    @Autowired
    private MessageSourceAccessor msg;

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, List<String>> handle(MethodArgumentNotValidException exception,
                                            HttpServletRequest httpRequest) {

        // todo: final Locale locale = httpRequest.getLocale();

        final BindingResult bindingResult = exception.getBindingResult();
        final List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        final List<FormValidationErrorViewModel.ParameterValidationError> errors = Lists.newArrayList();

        Map<String, List<String>> res = new HashMap<>();

        for (final FieldError fieldError : fieldErrors) {
            res.put(fieldError.getField(), asList(fieldError.getDefaultMessage()));
        }

        return res;
    }

    private String buildMessage(FieldError fe, Locale locale) {
        StringBuilder errorCode = new StringBuilder("");
        String localizedErrorMsg = "";
        errorCode.append("validation").append(".");
        errorCode.append(fe.getObjectName()).append(".");
        errorCode.append(fe.getField()).append(".");
        errorCode.append(fe.getCode().toLowerCase());

        try {
            String code = errorCode.toString();
            localizedErrorMsg = msg.getMessage(code, locale);
        } catch (Exception ex) {
            localizedErrorMsg = fe.getDefaultMessage();
        }
        return localizedErrorMsg;
    }
}
