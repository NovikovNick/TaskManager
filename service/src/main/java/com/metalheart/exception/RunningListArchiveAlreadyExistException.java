package com.metalheart.exception;

import com.metalheart.model.WeekId;

public class RunningListArchiveAlreadyExistException extends Exception {

    public RunningListArchiveAlreadyExistException(WeekId weekId) {
        super(String.format("Archive for %d week of %d already exist", weekId.getWeek(), weekId.getYear()));
    }
}
