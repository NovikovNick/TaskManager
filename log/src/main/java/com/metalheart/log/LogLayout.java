package com.metalheart.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.LayoutBase;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.metalheart.model.ExceptionDetail;
import com.metalheart.model.LogInfo;
import com.metalheart.model.RestRequestInfo;
import com.metalheart.model.RestResponseInfo;
import java.text.SimpleDateFormat;

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
                .setException(getExceptionDetail(event))
                .setRequest(getRestRequestInfo(event))
                .setResponse(getRestResponseInfo(event));
            log = objectMapper.writeValueAsString(logInfo);

        } catch (Exception e) {
            log = "{\"ERROR_MSG\":\"" + e.getMessage() + "\"}";
        }
        return log + CoreConstants.LINE_SEPARATOR;
    }

    private RestRequestInfo getRestRequestInfo(ILoggingEvent event) {

        if (isNull(event.getArgumentArray())) {
            return null;
        }
        for (Object arg : event.getArgumentArray()) {
            if (arg instanceof RestRequestInfo) {
                return (RestRequestInfo) arg;
            }
        }
        return null;
    }

    private RestResponseInfo getRestResponseInfo(ILoggingEvent event) {

        if (isNull(event.getArgumentArray())) {
            return null;
        }
        for (Object arg : event.getArgumentArray()) {
            if (arg instanceof RestResponseInfo) {
                return (RestResponseInfo) arg;
            }
        }
        return null;
    }

    private ExceptionDetail getExceptionDetail(ILoggingEvent event) {

        if (isNull(event.getThrowableProxy())) {
            return null;
        }

        return ExceptionDetail.builder()
            .stacktrace(ThrowableProxyUtil.asString(event.getThrowableProxy()))
            .message(event.getThrowableProxy().getMessage())
            .build();
    }
}
