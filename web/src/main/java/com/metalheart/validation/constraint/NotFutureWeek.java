package com.metalheart.validation.constraint;

import com.metalheart.validation.validator.NotFutureWeekValidator;
import com.metalheart.validation.validator.UniqueEmailValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy= NotFutureWeekValidator.class)
public @interface NotFutureWeek {

    String message() default "{com.metalheart.validation.constraint.NotFutureWeek.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}