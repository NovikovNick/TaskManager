package com.metalheart.model.request;

import com.metalheart.validation.constraint.NotFutureWeek;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
@NotFutureWeek
public class ArchiveRequest {

    @NotNull
    @Min(2000)
    @Max(2100)
    private Integer year;

    @NotNull
    @Min(0)
    @Max(52)
    private Integer week;

}
