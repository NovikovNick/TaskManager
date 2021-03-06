package com.metalheart.model.request;

import com.metalheart.log.LogContextField;
import com.metalheart.model.TaskStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WeekWorkLogUpdateRequest {

    @LogContextField(LogContextField.Field.TASK_ID)
    private Integer taskId;

    @LogContextField(LogContextField.Field.DAY_INDEX)
    private Integer dayIndex;

    @LogContextField(LogContextField.Field.STATUS)
    private TaskStatus status;
}
