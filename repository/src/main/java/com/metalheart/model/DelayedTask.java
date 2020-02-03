package com.metalheart.model;

import com.metalheart.log.LogContextField;
import java.io.Serializable;
import java.util.UUID;
import lombok.Data;

@Data
public class DelayedTask implements Serializable {

    @LogContextField(LogContextField.Field.DELAYED_TASK_ID)
    private UUID taskId;
}
