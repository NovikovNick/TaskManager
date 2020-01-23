package com.metalheart.service;

import com.metalheart.exception.NoSuchRunningListArchiveException;
import com.metalheart.model.RunningList;
import com.metalheart.model.WeekId;

public interface RunningListArchiveService {

    RunningList getPrev(WeekId weekId) throws NoSuchRunningListArchiveException;

    RunningList getNext(Integer userId, WeekId weekId) throws NoSuchRunningListArchiveException;

    boolean hasPreviousArchive(WeekId weekId);

    boolean isArchiveExist(WeekId weekId);

    RunningList save(RunningList archiveToSave);

    void delete(WeekId weekId);
}
