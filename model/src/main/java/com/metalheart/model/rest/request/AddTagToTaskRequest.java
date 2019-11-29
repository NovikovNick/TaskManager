package com.metalheart.model.rest.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class AddTagToTaskRequest {

    @NotNull
    @NotEmpty
    @Length(min = 3, max = 30)
    private String tag;

    @NotNull
    private Integer taskId;
}
