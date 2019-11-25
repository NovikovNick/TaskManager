package com.metalheart.repository.inmemory.impl;

import com.metalheart.model.RunningListAction;
import com.metalheart.repository.inmemory.RunningListCommandRepository;
import java.util.ArrayDeque;
import java.util.Deque;
import org.springframework.stereotype.Component;

@Component
public class RunningListCommandRepositoryImpl implements RunningListCommandRepository {

    private Deque<RunningListAction> doneActions;

    private Deque<RunningListAction> undoneActions;

    public RunningListCommandRepositoryImpl() {
        doneActions = new ArrayDeque<>();
        undoneActions = new ArrayDeque<>();
    }

    @Override
    public void addAction(RunningListAction action) {
        doneActions.addFirst(action);
        undoneActions.clear();
    }

    @Override
    public RunningListAction popDone() {
        return doneActions.pollFirst();
    }

    @Override
    public void pushUndone(RunningListAction action) {
        undoneActions.addFirst(action);
    }

    @Override
    public RunningListAction popUndone() {
        return undoneActions.pollFirst();
    }

    @Override
    public void pushDone(RunningListAction action) {
        doneActions.addFirst(action);
    }

    @Override
    public boolean hasDone() {
        return !doneActions.isEmpty();
    }

    @Override
    public boolean hasUndone() {
        return !undoneActions.isEmpty();
    }

    @Override
    public void clear() {
        doneActions.clear();
        undoneActions.clear();
    }
}
