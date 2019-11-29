package com.metalheart.service.impl;

import com.metalheart.model.WeekId;
import com.metalheart.model.rest.response.CalendarViewModel;
import com.metalheart.service.DateService;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.stereotype.Component;

@Component
public class DateServiceImpl implements DateService {

    private static DateTimeFormatter DAY_FORMATTER = DateTimeFormatter.ofPattern("dd");

    @Override
    public ZonedDateTime now() {
        return ZonedDateTime.now();
    }

    @Override
    public WeekId getCurrentWeekId() {
        return getWeekId(ZonedDateTime.now());
    }

    @Override
    public WeekId getNextWeekId(WeekId weekId) {
        return getWeekId(toZonedDateTime(weekId).plusWeeks(1));
    }

    @Override
    public WeekId getPreviousWeekId(WeekId weekId) {
        return getWeekId(toZonedDateTime(weekId).minusWeeks(1));
    }

    @Override
    public CalendarViewModel getCalendar() {

        var builder = CalendarViewModel.builder();

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime monday = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        List<String> weekDates = IntStream.range(0, 7)
            .mapToObj((i) -> DAY_FORMATTER.format(monday.plusDays(i)))
            .collect(Collectors.toList());

        return builder
            .currentDay(now.getDayOfWeek().getValue() - 1)
            .weekDates(weekDates)
            .build();
    }

    public WeekId getWeekId(ZonedDateTime zonedDateTime) {
        zonedDateTime = zonedDateTime.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        Integer week = zonedDateTime.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
        int year = zonedDateTime.getYear();
        return new WeekId(year, week);
    }

    public ZonedDateTime toZonedDateTime(WeekId weekId) {

        Integer year = weekId.getYear();
        Integer week = weekId.getWeek();

        return ZonedDateTime.of(LocalDate.ofYearDay(year, 1), LocalTime.now(), ZoneId.systemDefault())
            .with(IsoFields.WEEK_OF_WEEK_BASED_YEAR, week)
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }
}
