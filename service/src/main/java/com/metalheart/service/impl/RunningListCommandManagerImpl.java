package com.metalheart.service.impl;

import com.metalheart.exception.UnableToRedoException;
import com.metalheart.exception.UnableToUndoException;
import com.metalheart.model.RunningListAction;
import com.metalheart.repository.inmemory.RunningListCommandRepository;
import com.metalheart.service.RunningListCommandManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RunningListCommandManagerImpl implements RunningListCommandManager {

    @Autowired
    private RunningListCommandRepository commandRepository;

    @Override
    public <T> T execute(RunningListAction<T> action) {

        T res = action.execute();
        commandRepository.addAction(action);
        return res;
    }

    @Transactional
    @Override
    public void undo() throws UnableToUndoException {

        if (!canUndo()) {
            throw new UnableToUndoException();
        }

        RunningListAction action = commandRepository.popDone();
        action.undo();
        commandRepository.pushUndone(action);
    }

    @Override
    public void redo() throws UnableToRedoException {

        if (!canRedo()) {
            throw new UnableToRedoException();
        }

        RunningListAction action = commandRepository.popUndone();
        action.execute();
        commandRepository.pushDone(action);

    }

    @Override
    public boolean canRedo() {
        return commandRepository.hasUndone();
    }

    @Override
    public boolean canUndo() {
        return commandRepository.hasDone();
    }

    @Override
    public void clear() {
        commandRepository.clear();
    }
}
