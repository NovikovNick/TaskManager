package com.metalheart.exception;

import com.metalheart.model.WeekId;

public class RunningListArchiveAlreadyExistException extends Exception {

    public RunningListArchiveAlreadyExistException(Integer userId, WeekId weekId) {
        super(String.format("Archive for %d week of %d already exist for user %d",
            weekId.getWeek(), weekId.getYear(), userId));
    }
}
