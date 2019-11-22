package com.metalheart.service.impl;

import com.metalheart.model.RunningListAction;
import com.metalheart.service.RunningListCommandManager;
import java.util.ArrayDeque;
import java.util.Deque;
import org.springframework.stereotype.Component;

@Component
public class RunningListCommandManagerImpl implements RunningListCommandManager {

    private Deque<RunningListAction> actionStackNormal;

    private Deque<RunningListAction> actionStackReverse;

    public RunningListCommandManagerImpl() {
        clear();
    }

    @Override
    public void execute(RunningListAction action) {
        action.execute();
        actionStackNormal.addFirst(action);
    }

    @Override
    public void undo() {
        RunningListAction action = actionStackNormal.pollFirst();
        action.undo();
        actionStackReverse.addFirst(action);
    }

    @Override
    public void redo() {
        RunningListAction action = actionStackReverse.pollFirst();
        action.execute();
        actionStackNormal.addFirst(action);
    }

    @Override
    public boolean canRedo() {
        return !actionStackReverse.isEmpty();
    }

    @Override
    public boolean canUndo() {
        return !actionStackNormal.isEmpty();
    }

    @Override
    public void clear() {
        actionStackNormal = new ArrayDeque<>();
        actionStackReverse = new ArrayDeque<>();
    }
}
