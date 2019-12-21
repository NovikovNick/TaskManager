package com.metalheart.model.request;

import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class CRUDTagRequest {

    @NotNull
    @Length(min = 3, max = 30)
    private String tag;
}
