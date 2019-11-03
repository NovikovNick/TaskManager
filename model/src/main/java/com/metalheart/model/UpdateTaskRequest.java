package com.metalheart.model;

import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateTaskRequest {

    @NotNull
    private Integer id;

    @NotNull
    private String title;

    private String description;
}