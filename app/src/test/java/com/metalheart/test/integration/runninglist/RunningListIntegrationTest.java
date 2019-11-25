package com.metalheart.test.integration.runninglist;

import com.metalheart.model.rest.request.CreateTaskRequest;
import com.metalheart.model.rest.response.RunningListViewModel;
import com.metalheart.service.RunningListService;
import com.metalheart.service.TaskService;
import com.metalheart.test.integration.BaseIntegrationTest;
import java.time.Duration;
import java.time.Instant;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertNotNull;

public class RunningListIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private RunningListService runningListService;

    @Test
    public void test() {

        StringBuilder res = new StringBuilder();
        for (int i = 10; i <= 100; i *= 10) {

            for (int j = 0; j < i; j++) {
                taskService.createTask(generateRandomCreateTaskRequest(i + "_" + j));
            }

            Instant t0 = Instant.now();
            RunningListViewModel runningList = runningListService.getRunningList();
            assertNotNull(runningList);
            String x = Duration.between(t0, Instant.now()) + ": for " + i + "\n";
            res.append(x);

        }

        System.out.println(res);
    }


    private CreateTaskRequest generateRandomCreateTaskRequest(String prefix) {
        String taskTitle = "asdf" + prefix;
        String taskDescription = "asdfasdf" + prefix;

        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle(taskTitle);
        request.setDescription(taskDescription);
        return request;
    }
}
