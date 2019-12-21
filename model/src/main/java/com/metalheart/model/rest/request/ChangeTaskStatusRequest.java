package com.metalheart.model.rest.request;

import com.metalheart.log.LogContextField;
import com.metalheart.model.TaskStatus;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangeTaskStatusRequest {

    @LogContextField(LogContextField.Field.TASK_ID)
    @NotNull
    private Integer taskId;

    @NotNull
    private TaskStatus status;

    @NotNull
    private Integer dayIndex;
}