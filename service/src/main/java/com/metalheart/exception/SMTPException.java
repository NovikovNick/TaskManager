package com.metalheart.exception;

import lombok.Data;

@Data
public class SMTPException extends Exception {

    private String smtpLog;

    public SMTPException(String smtpLog, Throwable cause) {
        super(cause);
        this.smtpLog = smtpLog;
    }

    public SMTPException(Throwable cause) {
        super(cause);
    }
}
