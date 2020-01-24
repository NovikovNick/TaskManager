package com.metalheart.service;

import com.metalheart.exception.NoSuchRunningListArchiveException;
import com.metalheart.model.RunningList;
import com.metalheart.model.WeekId;

public interface RunningListArchiveService {

    RunningList getPrev(Integer userId, WeekId weekId) throws NoSuchRunningListArchiveException;

    RunningList getNext(Integer userId, WeekId weekId) throws NoSuchRunningListArchiveException;

    boolean hasPreviousArchive(Integer userId, WeekId weekId);

    boolean isArchiveExist(Integer userId, WeekId weekId);

    RunningList save(Integer userId, RunningList archiveToSave);

    void delete(Integer userId, WeekId weekId);
}
