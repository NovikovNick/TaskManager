package com.metalheart.model;

import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class TaskModel {

    private Integer id;

    private String title;

    private String description;

    private Integer priority;

    private ZonedDateTime createdAt;

    private ZonedDateTime modifiedAt;
}