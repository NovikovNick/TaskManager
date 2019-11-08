package com.metalheart.model.jpa;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class WeekWorkLogPK implements Serializable {

    @Column(name = "task_id", nullable = false)
    private Integer taskId;

    @Column(name = "day_index", nullable = false)
    private Integer dayIndex;
}
