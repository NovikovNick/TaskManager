package com.metalheart.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class WeekWorkLog {

    private Integer taskId;

    private Integer dayIndex;

    private TaskStatus status;
}
