package com.metalheart.service.impl;

import com.metalheart.model.Calendar;
import com.metalheart.model.RunningList;
import com.metalheart.model.Tag;
import com.metalheart.model.Task;
import com.metalheart.model.WeekId;
import com.metalheart.service.DateService;
import com.metalheart.service.RunningListArchiveService;
import com.metalheart.service.RunningListCommandManager;
import com.metalheart.service.RunningListService;
import com.metalheart.service.TagService;
import com.metalheart.service.TaskService;
import com.metalheart.service.WorkLogService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class RunningListServiceImpl implements RunningListService {

    @Autowired
    private RunningListArchiveService archiveService;

    @Autowired
    private RunningListCommandManager runningListCommandManager;

    @Autowired
    private TaskService taskService;

    @Autowired
    private DateService dateService;

    @Autowired
    private TagService tagService;

    @Autowired
    private WorkLogService workLogService;

    @Override
    public RunningList getRunningList(Integer userId, Integer timezoneOffset) {

        WeekId weekId = dateService.getCurrentWeekId(timezoneOffset);
        Calendar calendar = dateService.getCalendar(timezoneOffset);
        List<Task> tasks = getTaskWithStatuses(userId, calendar);
        boolean hasPrevious = archiveService.hasPreviousArchive(userId, weekId);
        boolean canUndo = runningListCommandManager.canUndo(userId);
        boolean canRedo = runningListCommandManager.canRedo(userId);
        List<Tag> selectedTags = tagService.getSelectedTags(userId);
        List<Tag> tags = tagService.getTags(userId);

        RunningList runningList = RunningList.builder()
            .calendar(calendar)
            .tasks(tasks)
            .editable(true)
            .hasNext(false)
            .hasPrevious(hasPrevious)
            .canUndo(canUndo)
            .canRedo(canRedo)
            .selectedTags(selectedTags)
            .allTags(tags)
            .weekId(weekId)
            .build();
        return runningList;
    }


    private List<Task> getTaskWithStatuses(Integer userId, Calendar calendar) {

        // todo: optimize
        List<Task> tasks = taskService.getTasks(userId);

        for (Task task : tasks) {
            task.setStatus(workLogService.get(task.getId(), calendar.getCurrentDay()));
        }
        return tasks;
    }
}
