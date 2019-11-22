package com.metalheart.service;

import com.metalheart.model.WeekId;
import com.metalheart.model.rest.response.RunningListViewModel;

public interface RunningListArchiveService {

    RunningListViewModel getPrev(Integer year, Integer week);

    RunningListViewModel getNext(Integer year, Integer week);

    void archive();

    boolean hasPreviousArchive(WeekId weekId);
}
