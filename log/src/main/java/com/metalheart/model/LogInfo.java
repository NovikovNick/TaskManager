
package com.metalheart.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Map;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LogInfo {

    private String date;
    private String time;
    private String level;
    private String msg;
    private String thread;
    private String className;

    private Map<String, String> context;

    private ExceptionDetail exception;

    private RestRequestInfo request;
    private RestResponseInfo response;
}
