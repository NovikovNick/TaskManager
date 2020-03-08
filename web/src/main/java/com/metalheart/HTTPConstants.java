package com.metalheart;

public class HTTPConstants {

    public static final int HTTP_UNPROCESSABLE_ENTITY = 422;
    public static final int HTTP_NOT_ACCEPTABLE = 406;

    public static final String HEADER_TIMEZONE_OFFSET = "TIMEZONE_OFFSET";

    private HTTPConstants() {
        throw new UnsupportedOperationException();
    }
}
