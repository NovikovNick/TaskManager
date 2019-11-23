package com.metalheart.service.impl;

import com.metalheart.exception.UnableToRedoException;
import com.metalheart.exception.UnableToUndoException;
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
    public void undo() throws UnableToUndoException {

        if (!canUndo()) {
            throw new UnableToUndoException();
        }

        RunningListAction action = actionStackNormal.pollFirst();
        action.undo();
        actionStackReverse.addFirst(action);
    }

    @Override
    public void redo() throws UnableToRedoException {

        if (!canRedo()) {
            throw new UnableToRedoException();
        }

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
