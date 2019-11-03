package com.metalheart.model;

import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangeTaskPriorityRequest {

    @NotNull
    private Integer startIndex;

    @NotNull
    private Integer endIndex;
}