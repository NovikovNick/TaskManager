package com.metalheart.model;

import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateTaskRequest {

    @NotNull
    private Integer taskId;

    @NotNull
    private String content;
}