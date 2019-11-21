package com.metalheart.service;

import com.metalheart.model.rest.response.RunningListViewModel;

public interface RunningListService {

    void archive();

    RunningListViewModel getRunningList();

    RunningListViewModel getPrev(Integer year, Integer week);

    RunningListViewModel getNext(Integer year, Integer week);

    RunningListViewModel redo();

    RunningListViewModel undo();
}
