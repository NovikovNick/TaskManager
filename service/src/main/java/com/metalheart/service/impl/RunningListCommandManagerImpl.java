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
    public <T> T execute(Integer userId, RunningListAction<T> action) {

        T res = action.execute();
        commandRepository.addAction(userId, action);
        return res;
    }

    @Transactional
    @Override
    public void undo(Integer userId) throws UnableToUndoException {

        if (!canUndo(userId)) {
            throw new UnableToUndoException();
        }

        RunningListAction action = commandRepository.popDone(userId);
        action.undo();
        commandRepository.pushUndone(userId, action);
    }

    @Override
    public void redo(Integer userId) throws UnableToRedoException {

        if (!canRedo(userId)) {
            throw new UnableToRedoException();
        }

        RunningListAction action = commandRepository.popUndone(userId);
        action.redo();
        commandRepository.pushDone(userId, action);

    }

    @Override
    public boolean canRedo(Integer userId) {
        return commandRepository.hasUndone(userId);
    }

    @Override
    public boolean canUndo(Integer userId) {
        return commandRepository.hasDone(userId);
    }

    @Override
    public void clear(Integer userId) {
        commandRepository.clear(userId);
    }
}
