package com.metalheart.test.integration;

import com.metalheart.App;
import com.metalheart.PostgresqlContainer;
import com.metalheart.model.WeekId;
import com.metalheart.model.jpa.Task;
import com.metalheart.model.jpa.TaskStatus;
import com.metalheart.model.rest.request.ChangeTaskStatusRequest;
import com.metalheart.model.rest.request.CreateTaskRequest;
import com.metalheart.model.rest.response.CalendarViewModel;
import com.metalheart.model.rest.response.TagViewModel;
import com.metalheart.repository.inmemory.SelectedTagRepository;
import com.metalheart.repository.jpa.RunningListArchiveJpaRepository;
import com.metalheart.repository.jpa.TaskJpaRepository;
import com.metalheart.repository.jpa.WeekWorkLogJpaRepository;
import com.metalheart.service.DateService;
import com.metalheart.service.RunningListCommandManager;
import com.metalheart.test.integration.config.DataSourceTestConfiguration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = DataSourceTestConfiguration.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = App.class
)
public abstract class BaseIntegrationTest {

    @ClassRule
    public static PostgresqlContainer POSTGRESQL = PostgresqlContainer.getInstance();

    @Autowired
    private RunningListArchiveJpaRepository archiveJpaRepository;

    @Autowired
    private WeekWorkLogJpaRepository workLogJpaRepository;

    @Autowired
    private TaskJpaRepository taskJpaRepository;

    @Autowired
    private RunningListCommandManager commandManager;

    @Autowired
    private SelectedTagRepository selectedTagRepository;

    @Before
    public void cleanUp() {
        archiveJpaRepository.deleteAll();
        workLogJpaRepository.deleteAll();
        taskJpaRepository.deleteAll();
        commandManager.clear();
        selectedTagRepository.deleteAll();
    }

    protected CreateTaskRequest generateRandomCreateTaskRequest() {
        return getCreateTaskRequest(RandomStringUtils.random(20));
    }


    protected CreateTaskRequest getCreateTaskRequest(String title) {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle(title);
        return request;
    }

    protected CreateTaskRequest getCreateTaskRequest(String title, String... tags) {

        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle(title);
        request.setTags(Arrays.stream(tags)
            .map(tag -> TagViewModel.builder().text(tag).build())
            .collect(Collectors.toList()));
        return request;
    }

    protected List<String> toStingList(TaskStatus... statuses) {
        return Arrays.stream(statuses).map(Objects::toString).collect(Collectors.toList());
    }

    protected void setDate(DateService dateServiceMock, int year, int week, int day) {
        CalendarViewModel calendar = CalendarViewModel.builder()
            .currentDay(day)
            .weekDates(Collections.emptyList())
            .build();
        when(dateServiceMock.getCalendar()).thenReturn(calendar);
        when(dateServiceMock.getPreviousWeekId(any())).thenReturn(WeekId.builder().year(year).week(week - 1).build());
        when(dateServiceMock.getCurrentWeekId()).thenReturn(WeekId.builder().year(year).week(week).build());
        when(dateServiceMock.getNextWeekId(any())).thenReturn(WeekId.builder().year(year).week(week + 1).build());
    }

    protected ChangeTaskStatusRequest getChangeStatusRequest(Task createdTask, int dayIndex, TaskStatus status) {
        ChangeTaskStatusRequest updateRequest = new ChangeTaskStatusRequest();
        updateRequest.setTaskId(createdTask.getId());
        updateRequest.setDayIndex(dayIndex);
        updateRequest.setStatus(status);
        return updateRequest;
    }
}
