package com.metalheart.test.integration;

import com.metalheart.App;
import com.metalheart.PostgresqlContainer;
import com.metalheart.model.Calendar;
import com.metalheart.model.Tag;
import com.metalheart.model.Task;
import com.metalheart.model.TaskStatus;
import com.metalheart.model.User;
import com.metalheart.model.WeekId;
import com.metalheart.model.jpa.UserJpa;
import com.metalheart.repository.inmemory.SelectedTagRepository;
import com.metalheart.repository.jpa.RunningListArchiveJpaRepository;
import com.metalheart.repository.jpa.TagJpaRepository;
import com.metalheart.repository.jpa.TaskJpaRepository;
import com.metalheart.repository.jpa.UserJpaRepository;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = DataSourceTestConfiguration.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = App.class
)
@ActiveProfiles("test")
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

    @Autowired
    private TagJpaRepository tagJpaRepository;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Before
    public void cleanUp() {
        selectedTagRepository.deleteAll();

        userJpaRepository.findAll().forEach(user -> commandManager.clear(user.getId()));
        userJpaRepository.deleteAll();

        archiveJpaRepository.deleteAll();
        workLogJpaRepository.deleteAll();
        taskJpaRepository.deleteAll();
        tagJpaRepository.deleteAll();
    }

    protected Task generateRandomTask(Integer userId) {
        return getTask(userId, RandomStringUtils.random(20));
    }


    protected Task getTask(Integer userId, String title) {
        return Task.builder().title(title).userId(userId).build();
    }

    protected Task getTask(Integer userId, String title, String... tags) {

        return Task.builder()
            .userId(userId)
            .title(title)
            .tags(Arrays.stream(tags)
                .map(tag -> Tag.builder().title(tag).build())
                .collect(Collectors.toList()))
            .build();
    }

    protected List<String> toStingList(TaskStatus... statuses) {
        return Arrays.stream(statuses).map(Objects::toString).collect(Collectors.toList());
    }

    protected void setDate(DateService dateServiceMock, int year, int week, int day) {
        Calendar calendar = Calendar.builder()
            .currentDay(day)
            .weekDates(Collections.emptyList())
            .build();
        when(dateServiceMock.getCalendar(anyInt())).thenReturn(calendar);
        when(dateServiceMock.getCalendar(any(WeekId.class))).thenReturn(calendar);
        when(dateServiceMock.getPreviousWeekId(any())).thenReturn(WeekId.builder().year(year).week(week - 1).build());
        when(dateServiceMock.getCurrentWeekId(null)).thenReturn(WeekId.builder().year(year).week(week).build());
        when(dateServiceMock.getNextWeekId(any())).thenReturn(WeekId.builder().year(year).week(week + 1).build());
    }

    protected Integer generateUser() {
        String username = RandomStringUtils.random(5);
        return userJpaRepository.save(UserJpa.builder()
            .email(username + "@mail.com")
            .username(username)
            .password(username)
            .build()).getId();
    }

    protected User createUser(String username) {
        return User.builder()
            .email(username + "@mail.com")
            .username(username)
            .password(username)
            .build();
    }
}
