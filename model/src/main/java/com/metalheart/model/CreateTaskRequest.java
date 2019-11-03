package com.metalheart.model;

import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateTaskRequest {

    @NotNull
    private String title;

    private String description;
}