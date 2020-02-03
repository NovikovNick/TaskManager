package com.metalheart.aop;

import com.metalheart.log.LogContextField;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

@Slf4j
@Aspect
@Component
public class LogOperationContextAspect {

    @Around("@annotation(com.metalheart.log.LogOperationContext)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        Arrays.stream(joinPoint.getArgs()).forEach(this::extractFieldToMDC);

        Object proceed = joinPoint.proceed();

        extractFieldToMDC(proceed);

        return proceed;
    }

    private void extractFieldToMDC(Object arg) {

        if (Objects.isNull(arg)) {
            return;
        }

        List<Field> fields = Arrays.stream(arg.getClass().getDeclaredFields()).collect(toList());

        if (Objects.nonNull(arg.getClass().getSuperclass())) {
            fields.addAll(Arrays.stream(arg.getClass().getSuperclass().getDeclaredFields()).collect(toList()));
        }

        for (Field field : fields) {
            LogContextField annotation = field.getAnnotation(LogContextField.class);

            if (Objects.nonNull(annotation)) {

                try {

                    field.setAccessible(true);
                    if (isNull(MDC.getCopyOfContextMap())) {
                        MDC.put("OPERATION_ID", UUID.randomUUID().toString());
                    }
                    MDC.put(annotation.value().toString(), Objects.toString(field.get(arg)));

                } catch (IllegalAccessException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }
}
