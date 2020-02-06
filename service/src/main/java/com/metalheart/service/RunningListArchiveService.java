package com.metalheart.service;

import com.metalheart.model.RunningList;
import com.metalheart.model.WeekId;
import java.util.Optional;

public interface RunningListArchiveService {

    Optional<RunningList> getPrev(Integer userId, WeekId weekId);

    Optional<RunningList> getNext(Integer userId, WeekId weekId);

    boolean hasPreviousArchive(Integer userId, WeekId weekId);

    Optional<RunningList> getArchive(Integer userId, WeekId weekId);

    boolean isArchiveExist(Integer userId, WeekId weekId);

    RunningList save(Integer userId, RunningList archiveToSave);

    void delete(Integer userId, WeekId weekId);
}
