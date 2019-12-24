package com.metalheart.validation.validator;

import com.metalheart.validation.constraint.EmailExist;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

public class EmailExistValidator implements ConstraintValidator<EmailExist, String> {

   /* @Autowired
    private EmailExistChecker emailChecker;*/

    @Override
    public void initialize(EmailExist constraintAnnotation) {

    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {


        if (StringUtils.isBlank(email)) {
            return true;
        }
        // return emailChecker.isEmailExist(email);
        return true;
    }
}
