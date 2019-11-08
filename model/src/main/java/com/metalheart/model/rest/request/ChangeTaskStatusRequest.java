package com.metalheart.model.rest.request;

import com.metalheart.model.jpa.TaskStatus;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangeTaskStatusRequest {

    @NotNull
    private Integer taskId;

    @NotNull
    private TaskStatus status;

    @NotNull
    private Integer dayIndex;
}