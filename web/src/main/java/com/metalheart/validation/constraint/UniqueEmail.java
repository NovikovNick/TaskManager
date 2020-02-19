package com.metalheart.validation.constraint;

import com.metalheart.validation.validator.UniqueEmailValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy= UniqueEmailValidator.class)
public @interface UniqueEmail {

    String message() default "{com.metalheart.validation.constraint.UniqueEmail.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}