package com.metalheart.service.impl;

import com.metalheart.exception.UnableToRedoException;
import com.metalheart.exception.UnableToUndoException;
import com.metalheart.log.LogContextField;
import com.metalheart.log.LogOperationContext;
import com.metalheart.model.RunningListAction;
import com.metalheart.repository.inmemory.RunningListCommandRepository;
import com.metalheart.service.RunningListCommandManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.metalheart.log.LogContextField.Field.USER_ID;

@Slf4j
@Component
public class RunningListCommandManagerImpl implements RunningListCommandManager {

    @Autowired
    private RunningListCommandRepository commandRepository;

    @LogOperationContext
    @Override
    public <T> T execute(@LogContextField(USER_ID) Integer userId, RunningListAction<T> action) {

        T res = action.execute();
        commandRepository.addAction(userId, action);
        return res;
    }

    @LogOperationContext
    @Transactional
    @Override
    public void undo(@LogContextField(USER_ID) Integer userId) throws UnableToUndoException {

        if (!canUndo(userId)) {
            throw new UnableToUndoException();
        }

        RunningListAction action = commandRepository.popDone(userId);
        action.undo();
        commandRepository.pushUndone(userId, action);
    }

    @LogOperationContext
    @Override
    public void redo(@LogContextField(USER_ID) Integer userId) throws UnableToRedoException {

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

    @LogOperationContext
    @Override
    public void clear(@LogContextField(USER_ID) Integer userId) {
        commandRepository.clear(userId);
        log.info("Clear command stack");
    }
}
