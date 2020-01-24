package com.metalheart.service;

import com.metalheart.exception.UnableToRedoException;
import com.metalheart.exception.UnableToUndoException;
import com.metalheart.model.RunningListAction;

public interface RunningListCommandManager {

    <T> T execute(Integer userId, RunningListAction<T> action);

    void redo(Integer userId) throws UnableToRedoException;

    boolean canRedo(Integer userId);

    void undo(Integer userId) throws UnableToUndoException;

    boolean canUndo(Integer userId);

    void clear(Integer userId);
}
