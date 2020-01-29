package com.metalheart.validation.validator;

import com.metalheart.model.request.PasswordAware;
import com.metalheart.validation.constraint.ConfirmedPassword;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class ConfirmedPasswordValidator implements ConstraintValidator<ConfirmedPassword, PasswordAware> {

    private ConfirmedPassword constraintAnnotation;

    @Override
    public void initialize(ConfirmedPassword constraintAnnotation) {
        this.constraintAnnotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(PasswordAware request, ConstraintValidatorContext context) {

        String password = request.getPassword();
        String confirmPassword = request.getConfirmPassword();

        if (isBlank(password) || isBlank(confirmPassword)) {
            return true;
        }

        if (!confirmPassword.equals(password)) {

            context
                .buildConstraintViolationWithTemplate(constraintAnnotation.message())
                .addPropertyNode("password")
                .addConstraintViolation()

                .buildConstraintViolationWithTemplate(constraintAnnotation.message())
                .addPropertyNode("confirmPassword")
                .addConstraintViolation()

                .disableDefaultConstraintViolation();

            return false;
        }

        return true;
    }
}
