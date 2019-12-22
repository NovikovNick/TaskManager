package com.metalheart.model;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeleteTaskRequest {

    private Task task;

    private List<WeekWorkLog> workLogs;
}
