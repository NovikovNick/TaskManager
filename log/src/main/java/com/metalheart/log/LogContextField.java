package com.metalheart.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogContextField {

    Field value();

    enum Field {
        OPERATION_ID,
        DELAYED_TASK_ID,
        USER_ID,
        EMAIL,
        TASK_ID,
        DAY_INDEX,
        STATUS;
    }
}
