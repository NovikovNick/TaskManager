package com.metalheart.service;

import com.metalheart.exception.NoSuchRunningListArchiveException;
import com.metalheart.model.RunningList;
import com.metalheart.model.WeekId;
import com.metalheart.model.jpa.RunningListArchiveJpa;

public interface RunningListArchiveService {

    RunningList getPrev(WeekId weekId) throws NoSuchRunningListArchiveException;

    RunningList getNext(WeekId weekId) throws NoSuchRunningListArchiveException;

    boolean hasPreviousArchive(WeekId weekId);

    boolean isArchiveExist(WeekId weekId);

    RunningListArchiveJpa save(RunningListArchiveJpa archiveToSave);

    void delete(RunningListArchiveJpa archive);
}
