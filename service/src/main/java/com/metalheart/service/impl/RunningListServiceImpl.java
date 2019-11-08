package com.metalheart.service.impl;

import com.metalheart.model.rest.response.CalendarViewModel;
import com.metalheart.model.rest.response.RunningListViewModel;
import com.metalheart.service.RunningListService;
import com.metalheart.service.TaskService;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RunningListServiceImpl implements RunningListService {

    private static DateTimeFormatter DAY_FORMATTER = DateTimeFormatter.ofPattern("dd");

    @Autowired
    private TaskService taskService;

    @Override
    public RunningListViewModel getRunningList() {
        return RunningListViewModel.builder()
            .calendar(getCalendar())
            .tasks(taskService.getTaskList())
            .build();
    }

    private CalendarViewModel getCalendar() {

        var builder = CalendarViewModel.builder();

        LocalDate now = LocalDate.now();
        LocalDate monday = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        List<String> weekDates = IntStream.range(0, 7)
            .mapToObj((i) -> DAY_FORMATTER.format(monday.plusDays(i)))
            .collect(Collectors.toList());

        return builder
            .currentDay(DAY_FORMATTER.format(now))
            .weekDates(weekDates)
            .build();
    }
}
