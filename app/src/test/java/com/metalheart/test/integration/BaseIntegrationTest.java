package com.metalheart.test.integration;

import com.metalheart.App;
import com.metalheart.PostgresqlContainer;
import com.metalheart.model.rest.request.CreateTaskRequest;
import com.metalheart.repository.jpa.RunningListArchiveJpaRepository;
import com.metalheart.repository.jpa.TaskJpaRepository;
import com.metalheart.repository.jpa.WeekWorkLogJpaRepository;
import com.metalheart.service.RunningListCommandManager;
import com.metalheart.test.integration.config.DataSourceTestConfiguration;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DataSourceTestConfiguration.class)
@SpringBootTest(classes = App.class)
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

    @Before
    public void cleanUp() {
        archiveJpaRepository.deleteAll();
        workLogJpaRepository.deleteAll();
        taskJpaRepository.deleteAll();
        commandManager.clear();
    }

    protected CreateTaskRequest generateRandomCreateTaskRequest() {
        return generateCreateTaskRequest(RandomStringUtils.random(20));
    }

    protected CreateTaskRequest generateCreateTaskRequest(String title) {

        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle(title);
        return request;
    }
}
