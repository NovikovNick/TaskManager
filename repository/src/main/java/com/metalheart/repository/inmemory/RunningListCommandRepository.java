package com.metalheart.repository.inmemory;

import com.metalheart.model.RunningListAction;

public interface RunningListCommandRepository {

    void addAction(Integer userId, RunningListAction action);

    RunningListAction popDone(Integer userId);

    RunningListAction popUndone(Integer userId);

    void pushUndone(Integer userId, RunningListAction action);

    void pushDone(Integer userId, RunningListAction action);

    boolean hasDone(Integer userId);

    boolean hasUndone(Integer userId);

    void clear(Integer userId);
}
