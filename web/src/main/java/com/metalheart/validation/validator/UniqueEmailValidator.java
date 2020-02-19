package com.metalheart.validation.validator;

import com.metalheart.model.User;
import com.metalheart.service.UserService;
import com.metalheart.validation.constraint.UniqueEmail;
import java.util.Objects;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    @Autowired
    private UserService userService;

    @Override
    public void initialize(UniqueEmail constraintAnnotation) {

    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {

        if (StringUtils.isBlank(email)) {
            return true;
        }

        // check if we try to update email for owner
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.nonNull(auth) && !(auth instanceof AnonymousAuthenticationToken)) {
            User user = (User) auth.getPrincipal();
            if (user.getEmail().equals(email)) {
                return true;
            }
        }

        return !userService.isUserExistByEmail(email);
    }
}
