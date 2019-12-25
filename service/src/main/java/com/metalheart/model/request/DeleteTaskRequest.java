package com.metalheart.model.request;

import com.metalheart.model.Task;
import com.metalheart.model.WeekWorkLog;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeleteTaskRequest {

    private Task task;

    private List<WeekWorkLog> workLogs;
}
