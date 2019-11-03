package com.metalheart.validation.constraint;

import com.metalheart.validation.validator.EmailExistValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy= EmailExistValidator.class)
public @interface EmailExist {

    String message() default "{EmailExist.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}