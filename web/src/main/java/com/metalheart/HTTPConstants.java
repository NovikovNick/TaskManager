package com.metalheart;

public class HTTPConstants {

    public static final int HTTP_UNPROCESSABLE_ENTITY = 422;
    public static final int HTTP_NOT_ACCEPTABLE = 406;

    public static final String HEADER_TIMEZONE_OFFSET = "TIMEZONE_OFFSET";

    public static final String MSG_OPERATION_UNDONE = "operation.undone";
    public static final String MSG_OPERATION_REDONE = "operation.redone";
    public static final String MSG_OPERATION_ARCHIVE = "operation.archive";
    public static final String MSG_OPERATION_SIGNIN = "operation.signin";
    public static final String MSG_ERROR_FORBIDDEN = "error.forbidden";
    public static final String MSG_ERROR_BADREQUEST = "error.badrequest";

    private HTTPConstants() {
        throw new UnsupportedOperationException();
    }
}
