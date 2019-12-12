package com.metalheart.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.LayoutBase;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.SimpleDateFormat;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import static java.util.Objects.isNull;

public class LogLayout extends LayoutBase<ILoggingEvent> {

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
    public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
    private ObjectMapper objectMapper;

    public LogLayout() {
        objectMapper = new ObjectMapper();
    }

    @Override
    public String doLayout(ILoggingEvent event) {

        String log;
        try {

            LogInfo logInfo = new LogInfo()
                .setLevel(event.getLevel().toString())
                .setClassName(event.getLoggerName())
                .setMsg(event.getMessage())
                .setDate(DATE_FORMAT.format(event.getTimeStamp()))
                .setTime(TIME_FORMAT.format(event.getTimeStamp()))
                .setThread(event.getThreadName())
                .setContext(event.getMDCPropertyMap())
                .setException(getExceptionDetail(event));
            log = objectMapper.writeValueAsString(logInfo);

        } catch (Exception e) {
            log = "{\"ERROR_MSG\":\"" + e.getMessage() + "\"}";
        }
        return log + CoreConstants.LINE_SEPARATOR;
    }

    private ExceptionDetail getExceptionDetail(ILoggingEvent event) {

        if(isNull(event.getThrowableProxy())) {
            return null;
        }

        return ExceptionDetail.builder()
            .stacktrace(ThrowableProxyUtil.asString(event.getThrowableProxy()))
            .message(event.getThrowableProxy().getMessage())
            .build();
    }

    @Data
    @Accessors(chain = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private static class LogInfo {

        private String date;
        private String time;
        private String level;
        private String msg;
        private String thread;
        private String className;
        private Map<String, String> context;
        private ExceptionDetail exception;
    }

    @Data
    @Builder
    private static class ExceptionDetail {
        private String message;
        private String stacktrace;
    }
}
