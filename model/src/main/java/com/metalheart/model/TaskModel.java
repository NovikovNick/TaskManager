package com.metalheart.model;

import com.metalheart.model.rest.response.TagViewModel;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.Data;

@Data
public class TaskModel {

    private Integer id;

    private String title;

    private String description;

    private Integer priority;

    private ZonedDateTime createdAt;

    private ZonedDateTime modifiedAt;

    private List<TagViewModel> tags;
}