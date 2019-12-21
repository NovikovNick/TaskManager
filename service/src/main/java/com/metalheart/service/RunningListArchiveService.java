package com.metalheart.service;

import com.metalheart.exception.NoSuchRunningListArchiveException;
import com.metalheart.model.WeekId;
import com.metalheart.model.jpa.RunningListArchiveJpa;
import com.metalheart.model.rest.response.RunningListViewModel;

public interface RunningListArchiveService {

    RunningListViewModel getPrev(WeekId weekId) throws NoSuchRunningListArchiveException;

    RunningListViewModel getNext(WeekId weekId) throws NoSuchRunningListArchiveException;

    boolean hasPreviousArchive(WeekId weekId);

    boolean isArchiveExist(WeekId weekId);

    RunningListArchiveJpa save(RunningListArchiveJpa archiveToSave);

    void delete(RunningListArchiveJpa archive);
}
