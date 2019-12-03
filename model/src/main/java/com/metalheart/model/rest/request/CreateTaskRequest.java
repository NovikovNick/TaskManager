package com.metalheart.model.rest.request;

import com.metalheart.model.rest.response.TagViewModel;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateTaskRequest {

    @NotNull
    @NotEmpty
    private String title;

    private String description;

    private List<TagViewModel> tags;
}