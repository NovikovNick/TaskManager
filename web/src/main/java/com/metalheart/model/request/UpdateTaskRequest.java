package com.metalheart.model.request;

import com.metalheart.model.response.TagViewModel;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateTaskRequest {

    @NotNull
    private Integer id;

    @NotNull
    @NotEmpty
    private String title;

    private String description;

    private List<TagViewModel> tags;
}