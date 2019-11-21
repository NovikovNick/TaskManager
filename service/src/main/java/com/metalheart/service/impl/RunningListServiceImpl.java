package com.metalheart.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.metalheart.model.jpa.RunningListArchive;
import com.metalheart.model.jpa.RunningListArchivePK;
import com.metalheart.model.jpa.Task;
import com.metalheart.model.jpa.TaskStatus;
import com.metalheart.model.jpa.WeekWorkLog;
import com.metalheart.model.rest.response.CalendarViewModel;
import com.metalheart.model.rest.response.RunningListViewModel;
import com.metalheart.model.rest.response.TaskViewModel;
import com.metalheart.repository.jpa.RunningListArchiveRepository;
import com.metalheart.repository.jpa.TaskJpaRepository;
import com.metalheart.repository.jpa.WeekWorkLogJpaRepository;
import com.metalheart.service.DateService;
import com.metalheart.service.RunningListCommandManager;
import com.metalheart.service.RunningListService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.metalheart.model.jpa.TaskStatus.CANCELED;
import static com.metalheart.model.jpa.TaskStatus.DELAYED;
import static com.metalheart.model.jpa.TaskStatus.DONE;
import static com.metalheart.model.jpa.TaskStatus.NONE;
import static com.metalheart.model.jpa.TaskStatus.TO_DO;


@Slf4j
@Component
public class RunningListServiceImpl implements RunningListService {

    @Autowired
    private RunningListArchiveRepository runningListArchiveRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RunningListCommandManager runningListCommandManager;

    @Autowired
    private TaskJpaRepository taskJpaRepository;

    @Autowired
    private WeekWorkLogJpaRepository weekWorkLogJpaRepository;

    @Autowired
    private DateService dateService;

    @Override
    public RunningListViewModel getRunningList() {

        RunningListArchivePK weekId = dateService.getCurrentWeekId();

        CalendarViewModel calendar = dateService.getCalendar();

        return RunningListViewModel.builder()
            .calendar(calendar)
            .tasks(getTaskList(calendar))
            .editable(true)
            .hasNext(false)
            .hasPrevious(hasPreviousArchive(weekId))
            .canUndo(runningListCommandManager.canUndo())
            .canRedo(runningListCommandManager.canRedo())
            .year(weekId.getYear())
            .week(weekId.getWeek())
            .build();
    }

    @Override
    public RunningListViewModel getPrev(Integer year, Integer week) {

        RunningListArchivePK weekId = RunningListArchivePK.builder().year(year).week(week).build();
        RunningListArchivePK prevWeekId = dateService.getPreviousWeekId(weekId);

        return getArchive(prevWeekId);
    }


    @Override
    public RunningListViewModel getNext(Integer year, Integer week) {

        RunningListArchivePK weekId = RunningListArchivePK.builder().year(year).week(week).build();
        RunningListArchivePK nextWeekId = dateService.getNextWeekId(weekId);

        if (dateService.getCurrentWeekId().equals(nextWeekId)) {
            return getRunningList();
        }

        return getArchive(nextWeekId);
    }

    @Override
    public void archive() {

        RunningListArchivePK weekId = dateService.getCurrentWeekId();
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
    public RunningListViewModel redo() {
        runningListCommandManager.redo();
        return getRunningList();
    }

    @Override
    public RunningListViewModel undo() {
        runningListCommandManager.undo();
        return getRunningList();
    }


    /* CONVENIENCE */

    private RunningListViewModel getArchive(RunningListArchivePK weekId) {
        RunningListViewModel runningListViewModel = getRunningListViewModel(weekId);
        runningListViewModel.setEditable(false);
        runningListViewModel.setHasPrevious(hasPreviousArchive(weekId));
        runningListViewModel.setHasNext(hasNextArchive(weekId));
        return runningListViewModel;
    }

    private boolean hasPreviousArchive(RunningListArchivePK weekId) {
        return isArchiveExist(dateService.getPreviousWeekId(weekId));
    }

    private boolean hasNextArchive(RunningListArchivePK weekId) {
        RunningListArchivePK nextWeekId = dateService.getNextWeekId(weekId);
        if (dateService.getCurrentWeekId().equals(nextWeekId)) {
            return true;
        }

        return isArchiveExist(nextWeekId);
    }

    private boolean isArchiveExist(RunningListArchivePK weekId) {
        return runningListArchiveRepository.existsById(weekId);
    }

    private RunningListViewModel getRunningListViewModel(RunningListArchivePK weekId) {
        RunningListArchive archive = runningListArchiveRepository.getOne(weekId);

        try {
            return objectMapper.readValue(archive.getArchive(), RunningListViewModel.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<TaskViewModel> getTaskList(CalendarViewModel calendar) {

        return taskJpaRepository.findAllByOrderByPriorityAsc().stream()
            .map(task -> TaskViewModel.builder()
                .id(task.getId())
                .status(getDayStatuses(task, calendar.getCurrentDay()))
                .title(task.getTitle())
                .description(task.getDescription())
                .build())
            .collect(Collectors.toList());
    }

    private List<String> getDayStatuses(Task task, Integer currentDay) {
        List<WeekWorkLog> taskWorkLog = weekWorkLogJpaRepository.findAllByTaskId(task.getId());

        List<String> res = new ArrayList<>();

        TaskStatus previous = NONE;
        for (int day = 0; day < 7; day++) {
            TaskStatus status = getStatus(taskWorkLog, day);

            if (NONE.equals(status) && previous.equals(NONE)) {

                res.add(NONE.toString());

            } else if (NONE.equals(status) && previous.equals(DONE)) {

                res.add(DONE.toString());

            } else if (DONE.equals(status)) {

                if (!DONE.equals(previous)) {
                    previous = DONE;
                }
                res.add(DONE.toString());

            } else if (NONE.equals(status) && previous.equals(CANCELED)) {

                res.add(CANCELED.toString());

            } else if (CANCELED.equals(status)) {

                if (!CANCELED.equals(previous)) {
                    previous = CANCELED;
                }
                res.add(CANCELED.toString());

            } else if (TO_DO.equals(status) && currentDay > day) {

                res.add(DELAYED.toString());

            } else {

                res.add(status.toString());
            }

        }
        return res;
    }

    private TaskStatus getStatus(List<WeekWorkLog> taskWorkLog, int day) {
        for (WeekWorkLog log : taskWorkLog) {
            if (log.getId().getDayIndex() == day) {
                return log.getStatus();
            }
        }
        return NONE;
    }
}
