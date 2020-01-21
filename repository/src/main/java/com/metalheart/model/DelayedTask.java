package com.metalheart.model;

import java.io.Serializable;
import java.util.UUID;
import lombok.Data;

@Data
public class DelayedTask implements Serializable {
    private UUID taskId;
}
