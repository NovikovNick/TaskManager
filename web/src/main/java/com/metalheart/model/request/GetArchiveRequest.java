package com.metalheart.model.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Data;

@Data
public class GetArchiveRequest {

    @NotNull
    @Min(2019)
    @Max(2119)
    private Integer year;

    @NotNull
    @Positive
    @Max(53)
    private Integer week;
}