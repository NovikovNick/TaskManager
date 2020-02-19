package com.metalheart.validation.validator;

import com.metalheart.model.User;
import com.metalheart.service.UserService;
import com.metalheart.validation.constraint.UniqueLogin;
import java.util.Objects;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class UniqueLoginValidator implements ConstraintValidator<UniqueLogin, String> {

    @Autowired
    private UserService userService;

    @Override
    public void initialize(UniqueLogin constraintAnnotation) {

    }

    @Override
    public boolean isValid(String login, ConstraintValidatorContext context) {

        if (StringUtils.isBlank(login)) {
            return true;
        }

        // check if we try to update login for owner
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.nonNull(auth) && !(auth instanceof AnonymousAuthenticationToken)) {
            User user = (User) auth.getPrincipal();
            if (user.getUsername().equals(login)) {
                return true;
            }
        }

        return !userService.findByUsername(login).isPresent();
    }
}
