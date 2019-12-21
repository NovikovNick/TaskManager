package com.metalheart.model;

import com.metalheart.model.service.TaskModel;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeleteTaskRequest {

    private TaskModel task;

    private List<WeekWorkLog> workLogs;
}
