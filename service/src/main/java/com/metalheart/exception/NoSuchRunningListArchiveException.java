package com.metalheart.exception;

import com.metalheart.model.WeekId;

public class NoSuchRunningListArchiveException extends Exception {

    public NoSuchRunningListArchiveException(Integer userId, WeekId weekId) {
        super(String.format("There is no user id  %d archive for %d week of %d",
            userId, weekId.getWeek(), weekId.getYear()));
    }
}
