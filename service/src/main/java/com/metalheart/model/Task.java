package com.metalheart.model;

import com.metalheart.log.LogContextField;
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
@Builder(toBuilder = true)
public class Task implements Cloneable {

    @LogContextField(LogContextField.Field.TASK_ID)
    private Integer id;

    private Integer userId;

    private String title;

    private String description;

    private Integer priority;

    private ZonedDateTime createdAt;

    private ZonedDateTime modifiedAt;

    private List<Tag> tags;

    private List<WeekWorkLog> status;

    @Override
    public Task clone() {

        List<Tag> tags = isNull(this.tags) ? emptyList() : this.tags.stream()
            .map(Tag::clone)
            .collect(Collectors.toList());

        return toBuilder()
            .tags(tags)
            .build();
    }
}