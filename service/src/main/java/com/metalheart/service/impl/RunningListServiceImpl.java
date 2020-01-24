package com.metalheart.service.impl;

import com.metalheart.model.Calendar;
import com.metalheart.model.RunningList;
import com.metalheart.model.Task;
import com.metalheart.model.WeekId;
import com.metalheart.service.DateService;
import com.metalheart.service.RunningListArchiveService;
import com.metalheart.service.RunningListCommandManager;
import com.metalheart.service.RunningListService;
import com.metalheart.service.TagService;
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
    private TaskServiceImpl taskService;

    @Autowired
    private DateService dateService;

    @Autowired
    private TagService tagService;

    @Autowired
    private WorkLogService workLogService;

    @Override
    public RunningList getRunningList(Integer userId) {

        WeekId weekId = dateService.getCurrentWeekId();

        Calendar calendar = dateService.getCalendar();

        return RunningList.builder()
            .calendar(calendar)
            .tasks(getTaskWithStatuses(userId, calendar))
            .editable(true)
            .hasNext(false)
            .hasPrevious(archiveService.hasPreviousArchive(userId, weekId))
            .canUndo(runningListCommandManager.canUndo(userId))
            .canRedo(runningListCommandManager.canRedo(userId))
            .selectedTags(tagService.getSelectedTags(userId))
            .allTags(tagService.getTags(userId))
            .weekId(weekId)
            .build();
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
