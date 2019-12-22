package com.metalheart.model.request;

import com.metalheart.model.rest.response.TagViewModel;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateTaskRequest {

    @NotNull
    private Integer id;

    @NotNull
    private String title;

    private String description;

    private List<TagViewModel> tags;
}