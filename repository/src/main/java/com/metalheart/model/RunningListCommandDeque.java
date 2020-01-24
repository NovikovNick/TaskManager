package com.metalheart.model;

import java.util.ArrayDeque;
import java.util.Deque;

public class RunningListCommandDeque {

    private Deque<RunningListAction> doneActions = new ArrayDeque<>();

    private Deque<RunningListAction> undoneActions = new ArrayDeque<>();

    public void addAction(RunningListAction action) {
        doneActions.addFirst(action);
        undoneActions.clear();
    }

    public RunningListAction popDone() {
        return doneActions.pollFirst();
    }

    public void pushUndone(RunningListAction action) {
        undoneActions.addFirst(action);
    }

    public RunningListAction popUndone() {
        return undoneActions.pollFirst();
    }

    public void pushDone(RunningListAction action) {
        doneActions.addFirst(action);
    }

    public boolean hasDone() {
        return !doneActions.isEmpty();
    }

    public boolean hasUndone() {
        return !undoneActions.isEmpty();
    }

    public void clear() {
        doneActions.clear();
        undoneActions.clear();
    }
}
