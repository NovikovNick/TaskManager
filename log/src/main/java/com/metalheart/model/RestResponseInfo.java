package com.metalheart.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestResponseInfo {

    private int status;
    private Map<String, String> headers;
    private String payload;
}
