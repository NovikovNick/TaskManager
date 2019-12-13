package com.metalheart.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExceptionDetail {
    private String message;
    private String stacktrace;
}
