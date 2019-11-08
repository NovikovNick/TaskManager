package com.metalheart.model.rest.request;

import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangeTaskPriorityRequest {

    @NotNull
    private Integer startIndex;

    @NotNull
    private Integer endIndex;
}