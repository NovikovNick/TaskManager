package com.metalheart.exception;

import com.metalheart.model.WeekId;

public class NoSuchRunningListArchiveException extends Exception {

    public NoSuchRunningListArchiveException(WeekId weekId) {
        super(String.format("There is no such archive for %d week of %d", weekId.getWeek(), weekId.getYear()));
    }
}
