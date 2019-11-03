package com.metalheart.rest;

import com.google.common.collect.Lists;
import com.metalheart.model.FormValidationErrorViewModel;
import java.util.List;
import java.util.Locale;
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

@Slf4j
@RestControllerAdvice
@Component
public class ControllerExceptionHandler {

    @Autowired
    private MessageSourceAccessor msg;

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public FormValidationErrorViewModel handle(MethodArgumentNotValidException exception,
                                               HttpServletRequest httpRequest) {

        final Locale locale = httpRequest.getLocale();
        final BindingResult bindingResult = exception.getBindingResult();
        final List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        final List<FormValidationErrorViewModel.ParameterValidationError> errors = Lists.newArrayList();

        for (final FieldError fieldError : fieldErrors) {
            var error = new FormValidationErrorViewModel.ParameterValidationError();
            error.setParameter(fieldError.getField());
            String code = fieldError.getCode();

            /*if (fieldError instanceof ValidationFieldError) {
                final String template
                    = ((ValidationFieldError) fieldError).getConstraintViolation().getMessageTemplate();
                error.setMessageCode(removeCurlyBraces(template));
            } else {
                error.setMessageCode(removeCurlyBraces(fieldError.getDefaultMessage()));
            }*/


            error.setMessage(fieldError.getDefaultMessage());

            //fieldError.
            errors.add(error);
        }

        FormValidationErrorViewModel response = new FormValidationErrorViewModel();
        response.setErrors(errors);
        return response;
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
