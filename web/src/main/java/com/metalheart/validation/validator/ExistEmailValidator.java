package com.metalheart.validation.validator;

import com.metalheart.service.UserService;
import com.metalheart.validation.constraint.ExistEmail;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class ExistEmailValidator implements ConstraintValidator<ExistEmail, String> {

    @Autowired
    private UserService userService;

    @Override
    public void initialize(ExistEmail constraintAnnotation) {

    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {

        if (StringUtils.isBlank(email)) {
            return true;
        }

        return userService.isUserExistByEmail(email);
    }
}
