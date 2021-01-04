package com.metalheart.model.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class CRUDTagRequest {

    @NotNull
    @Size(min = 3, max = 30)
    private String tag;
}
