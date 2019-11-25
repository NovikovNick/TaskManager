package com.metalheart.service;

import com.metalheart.exception.UnableToRedoException;
import com.metalheart.exception.UnableToUndoException;
import com.metalheart.model.RunningListAction;

public interface RunningListCommandManager {

    <T> T execute(RunningListAction<T> action);

    void redo() throws UnableToRedoException;

    boolean canRedo();

    void undo() throws UnableToUndoException;

    boolean canUndo();

    void clear();
}
