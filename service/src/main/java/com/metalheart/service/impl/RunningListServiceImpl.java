package com.metalheart.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.metalheart.model.jpa.RunningListArchive;
import com.metalheart.model.jpa.RunningListArchivePK;
import com.metalheart.model.rest.response.CalendarViewModel;
import com.metalheart.model.rest.response.RunningListViewModel;
import com.metalheart.repository.jpa.RunningListArchiveRepository;
import com.metalheart.service.RunningListCommandManager;
import com.metalheart.service.RunningListService;
import com.metalheart.service.TaskService;
import java.io.IOException;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RunningListServiceImpl implements RunningListService {

    private static DateTimeFormatter DAY_FORMATTER = DateTimeFormatter.ofPattern("dd");

    @Autowired
    private TaskService taskService;

    @Autowired
    private RunningListArchiveRepository runningListArchiveRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RunningListCommandManager runningListCommandManager;

    @Override
    public RunningListViewModel getRunningList() {

        RunningListArchivePK pk = getWeekId(ZonedDateTime.now());
        Integer week = pk.getWeek();
        Integer year = pk.getYear();

        return RunningListViewModel.builder()
            .calendar(getCalendar())
            .tasks(taskService.getTaskList())
            .editable(true)
            .hasNext(false)
            .hasPrevious(hasPrevious(getZonedDateTime(year, week)))
            .canUndo(runningListCommandManager.canUndo())
            .canRedo(runningListCommandManager.canRedo())
            .year(year)
            .week(week)
            .build();
    }

    @Override
    public RunningListViewModel getPrev(Integer year, Integer week) {

        ZonedDateTime desiredDate = getZonedDateTime(year, week).minusWeeks(1);

        RunningListViewModel runningListViewModel = getRunningListViewModel(getWeekId(desiredDate));
        runningListViewModel.setEditable(false);
        runningListViewModel.setHasPrevious(hasPrevious(desiredDate));
        runningListViewModel.setHasNext(hasNext(desiredDate));
        return runningListViewModel;
    }

    @Override
    public RunningListViewModel getNext(Integer year, Integer week) {


        ZonedDateTime desiredDate = getZonedDateTime(year, week).plusWeeks(1);

        RunningListArchivePK weekId = getWeekId(desiredDate);

        if (weekId.equals(getWeekId(ZonedDateTime.now()))) {
            return getRunningList();
        }

        RunningListViewModel runningListViewModel = getRunningListViewModel(weekId);
        runningListViewModel.setEditable(false);
        runningListViewModel.setHasPrevious(hasPrevious(desiredDate));
        runningListViewModel.setHasNext(hasNext(desiredDate));
        return runningListViewModel;
    }

    @Override
    public void archive() {

        ZonedDateTime now = ZonedDateTime.now();
        RunningListArchivePK weekId = getWeekId(now);
        if (runningListArchiveRepository.existsById(weekId)) {
            throw new RuntimeException("archive already exist! weekId = " + weekId);
        }

        RunningListViewModel runningList = getRunningList();

        try {
            RunningListArchive archive = runningListArchiveRepository.save(RunningListArchive.builder()
                .id(weekId)
                .archive(objectMapper.writeValueAsString(runningList))
                .build());
            log.info("Archive has been saved {}", archive);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public RunningListArchivePK getWeekId(ZonedDateTime zonedDateTime) {
        zonedDateTime = zonedDateTime.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        Integer week = zonedDateTime.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
        int year = zonedDateTime.getYear();
        return RunningListArchivePK.builder()
            .year(year)
            .week(week)
            .build();
    }

    @Override
    public RunningListViewModel redo() {
        runningListCommandManager.redo();
        return getRunningList();
    }

    @Override
    public RunningListViewModel undo() {
        runningListCommandManager.undo();
        return getRunningList();
    }

    private RunningListViewModel getRunningListViewModel(RunningListArchivePK weekId) {
        RunningListArchive archive = runningListArchiveRepository.getOne(weekId);

        try {
            return objectMapper.readValue(archive.getArchive(), RunningListViewModel.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private CalendarViewModel getCalendar() {

        var builder = CalendarViewModel.builder();

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime monday = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        List<String> weekDates = IntStream.range(0, 7)
            .mapToObj((i) -> DAY_FORMATTER.format(monday.plusDays(i)))
            .collect(Collectors.toList());

        return builder
            .currentDay(DAY_FORMATTER.format(now))
            .weekDates(weekDates)
            .build();
    }

    private boolean hasNext(ZonedDateTime desiredDate) {

        RunningListArchivePK weekId = getWeekId(desiredDate.plusWeeks(1));

        if (weekId.equals(getWeekId(ZonedDateTime.now()))) {
            return true;
        }

        return runningListArchiveRepository.existsById(weekId);
    }

    private boolean hasPrevious(ZonedDateTime desiredDate) {
        return runningListArchiveRepository.existsById(getWeekId(desiredDate.minusWeeks(1)));
    }

    private ZonedDateTime getZonedDateTime(Integer year, Integer week) {
        return ZonedDateTime.of(LocalDate.ofYearDay(year, 1), LocalTime.now(), ZoneId.systemDefault())
            .with(IsoFields.WEEK_OF_WEEK_BASED_YEAR, week)
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }
}
