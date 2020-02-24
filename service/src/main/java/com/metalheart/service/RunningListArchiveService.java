package com.metalheart.service;

import com.metalheart.model.RunningList;
import com.metalheart.model.WeekId;
import java.util.List;
import java.util.Optional;

public interface RunningListArchiveService {

    Optional<RunningList> getPrev(Integer userId, WeekId weekId, Integer timezoneOffset);

    Optional<RunningList> getNext(Integer userId, WeekId weekId, Integer timezoneOffset);

    boolean hasPreviousArchive(Integer userId, WeekId weekId);

    Optional<RunningList> getArchive(Integer userId, WeekId weekId, Integer timezoneOffset);

    boolean isArchiveExist(Integer userId, WeekId weekId);

    RunningList save(Integer userId, RunningList archiveToSave);

    void delete(Integer userId, WeekId weekId);

    List<WeekId> getExistingArchivesWeekIds(Integer userId);
}
