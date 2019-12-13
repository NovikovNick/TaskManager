package com.metalheart.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestRequestInfo {
    private String method;
    private String requestURI;
    private Map<String, String> headers;
    private String payload;
}
