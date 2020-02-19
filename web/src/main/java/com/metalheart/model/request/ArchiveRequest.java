package com.metalheart.model.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
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
