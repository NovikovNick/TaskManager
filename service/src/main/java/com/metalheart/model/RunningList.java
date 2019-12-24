package com.metalheart.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RunningList {

    private WeekId weekId;

    private Boolean editable;
    private Boolean hasNext;
    private Boolean hasPrevious;
    private Boolean canUndo;
    private Boolean canRedo;

    private Calendar calendar;
    private List<Task> tasks;

    private List<Tag> selectedTags;
    private List<Tag> allTags;
}
