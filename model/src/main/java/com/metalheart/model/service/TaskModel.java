package com.metalheart.model.service;

import com.metalheart.log.LogContextField;
import com.metalheart.model.rest.response.TagViewModel;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static java.util.Collections.emptyList;
import static java.util.Objects.isNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskModel implements Cloneable {

    @LogContextField(LogContextField.Field.TASK_ID)
    private Integer id;

    private String title;

    private String description;

    private Integer priority;

    private ZonedDateTime createdAt;

    private ZonedDateTime modifiedAt;

    private List<TagViewModel> tags;

    @Override
    public TaskModel clone() {

        List<TagViewModel> tags = isNull(this.tags) ? emptyList() : this.tags.stream()
            .map(TagViewModel::clone)
            .collect(Collectors.toList());

        return TaskModel.builder()
            .id(id)
            .title(title)
            .description(description)
            .priority(priority)
            .createdAt(createdAt)
            .modifiedAt(modifiedAt)
            .tags(tags)
            .build();
    }
}