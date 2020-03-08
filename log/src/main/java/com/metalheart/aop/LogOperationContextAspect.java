package com.metalheart.aop;

import com.metalheart.log.LogContextField;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import static com.metalheart.log.LogContextField.Field.OPERATION_ID;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

@Slf4j
@Aspect
@Component
public class LogOperationContextAspect {

    @Around("@annotation(com.metalheart.log.LogOperationContext)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        addInputToLogContext(joinPoint);

        Object output = joinPoint.proceed();

        extractFieldToLogContext(output);

        return output;
    }

    private void addInputToLogContext(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getStaticPart().getSignature();
        Method method = methodSignature.getMethod();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        for (int argIndex = 0; argIndex < args.length; argIndex++) {

            Object arg = args[argIndex];

            for (Annotation annotation : parameterAnnotations[argIndex]) {

                if (annotation instanceof LogContextField) {
                    addToLogContext(((LogContextField) annotation).value(), arg);
                }
            }
            extractFieldToLogContext(arg);
        }
    }

    private void extractFieldToLogContext(Object arg) {

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
                    addToLogContext(annotation.value(), field.get(arg));

                } catch (IllegalAccessException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    private void addToLogContext(LogContextField.Field key, Object value) {
        if (isNull(MDC.getCopyOfContextMap())) {
            MDC.put(OPERATION_ID.toString(), UUID.randomUUID().toString());
        }
        MDC.put(key.toString(), Objects.toString(value));
    }
}
