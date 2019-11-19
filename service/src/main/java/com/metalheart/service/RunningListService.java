package com.metalheart.service;

import com.metalheart.model.jpa.RunningListArchivePK;
import com.metalheart.model.rest.response.RunningListViewModel;
import java.time.ZonedDateTime;

public interface RunningListService {

    void archive();

    RunningListViewModel getRunningList();

    RunningListViewModel getPrev(Integer year, Integer week);

    RunningListViewModel getNext(Integer year, Integer week);

    RunningListArchivePK getWeekId(ZonedDateTime zonedDateTime);

    RunningListViewModel redo();

    RunningListViewModel undo();
}
