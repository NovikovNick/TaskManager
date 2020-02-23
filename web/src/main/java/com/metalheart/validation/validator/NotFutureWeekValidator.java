package com.metalheart.validation.validator;

import com.metalheart.model.WeekId;
import com.metalheart.model.request.ArchiveRequest;
import com.metalheart.service.DateService;
import com.metalheart.validation.constraint.NotFutureWeek;
import java.util.Objects;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class NotFutureWeekValidator implements ConstraintValidator<NotFutureWeek, ArchiveRequest> {

    @Autowired
    private DateService dateService;

    private NotFutureWeek constraintAnnotation;

    @Override
    public void initialize(NotFutureWeek constraintAnnotation) {
        this.constraintAnnotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(ArchiveRequest request, ConstraintValidatorContext context) {

        if (Objects.isNull(request.getYear()) || Objects.isNull(request.getWeek())) {
            return true;
        }

        WeekId current = dateService.getCurrentWeekId(0);

        if ((request.getYear() > current.getYear())) {
            context
                .buildConstraintViolationWithTemplate(constraintAnnotation.message())
                .addPropertyNode("year")
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
            return false;
        }

        if ((request.getWeek() > current.getWeek())) {
            context
                .buildConstraintViolationWithTemplate(constraintAnnotation.message())
                .addPropertyNode("week")
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
            return false;
        }

        return true;
    }
}
