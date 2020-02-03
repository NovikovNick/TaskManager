package com.metalheart.model;

import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExceptionDetail {
    private String message;
    private String stacktrace;
    private Map<String, Object> details;
}
