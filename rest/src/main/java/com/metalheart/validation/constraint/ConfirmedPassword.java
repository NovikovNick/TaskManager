package com.metalheart.validation.constraint;

import com.metalheart.validation.validator.ConfirmedPasswordValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy= ConfirmedPasswordValidator.class)
public @interface ConfirmedPassword {

    String message() default "{com.metalheart.validation.constraint.ConfirmedPassword.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}