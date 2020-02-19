package com.metalheart.model.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RunningListViewModel {

    private Integer year;
    private Integer week;

    private Boolean editable;
    private Boolean hasNext;
    private Boolean hasPrevious;
    private Boolean canUndo;
    private Boolean canRedo;

    private CalendarViewModel calendar;
    private List<TaskViewModel> tasks;

    private List<TagViewModel> selectedTags;
    private List<TagViewModel> allTags;
}
