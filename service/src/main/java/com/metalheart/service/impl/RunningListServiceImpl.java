package com.metalheart.service.impl;

import com.metalheart.model.WeekId;
import com.metalheart.model.jpa.Task;
import com.metalheart.model.jpa.TaskStatus;
import com.metalheart.model.jpa.WeekWorkLog;
import com.metalheart.model.rest.response.CalendarViewModel;
import com.metalheart.model.rest.response.RunningListViewModel;
import com.metalheart.model.rest.response.TaskViewModel;
import com.metalheart.repository.jpa.TaskJpaRepository;
import com.metalheart.repository.jpa.WeekWorkLogJpaRepository;
import com.metalheart.service.DateService;
import com.metalheart.service.RunningListArchiveService;
import com.metalheart.service.RunningListCommandManager;
import com.metalheart.service.RunningListService;
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
    private RunningListArchiveService archiveService;

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

        WeekId weekId = dateService.getCurrentWeekId();

        CalendarViewModel calendar = dateService.getCalendar();

        return RunningListViewModel.builder()
            .calendar(calendar)
            .tasks(getTaskList(calendar))
            .editable(true)
            .hasNext(false)
            .hasPrevious(archiveService.hasPreviousArchive(weekId))
            .canUndo(runningListCommandManager.canUndo())
            .canRedo(runningListCommandManager.canRedo())
            .year(weekId.getYear())
            .week(weekId.getWeek())
            .build();
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
