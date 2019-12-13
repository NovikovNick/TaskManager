package com.metalheart.model.service;

import com.metalheart.model.TaskModel;
import com.metalheart.model.jpa.WeekWorkLog;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeleteTaskRequest {

    private TaskModel task;

    private List<WeekWorkLog> workLogs;
}
