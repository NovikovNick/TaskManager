package com.metalheart.service;

import com.metalheart.exception.NoSuchRunningListArchiveException;
import com.metalheart.exception.RunningListArchiveAlreadyExistException;
import com.metalheart.model.WeekId;
import com.metalheart.model.rest.response.RunningListViewModel;

public interface RunningListArchiveService {

    RunningListViewModel getPrev(WeekId weekId) throws NoSuchRunningListArchiveException;

    RunningListViewModel getNext(WeekId weekId) throws NoSuchRunningListArchiveException;

    boolean hasPreviousArchive(WeekId weekId);

    /**
     * Can be undone
     * @param weekId
     * @throws RunningListArchiveAlreadyExistException
     */
    void archive(WeekId weekId) throws RunningListArchiveAlreadyExistException;
}
