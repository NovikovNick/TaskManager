package com.metalheart.model;

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