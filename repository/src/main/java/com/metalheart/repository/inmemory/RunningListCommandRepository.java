package com.metalheart.repository.inmemory;

import com.metalheart.model.RunningListAction;

public interface RunningListCommandRepository {

    void addAction(RunningListAction action);

    RunningListAction popDone();

    RunningListAction popUndone();

    void pushUndone(RunningListAction action);

    void pushDone(RunningListAction action);

    boolean hasDone();

    boolean hasUndone();

    void clear();
}
