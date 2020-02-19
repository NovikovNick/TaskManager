package com.metalheart.validation.constraint;

import com.metalheart.validation.validator.ExistEmailValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy= ExistEmailValidator.class)
public @interface ExistEmail {

    String message() default "{com.metalheart.validation.constraint.ExistEmail.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}