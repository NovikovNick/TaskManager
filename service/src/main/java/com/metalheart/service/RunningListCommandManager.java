package com.metalheart.service;

import com.metalheart.model.action.RunningListAction;

public interface RunningListCommandManager {

    void execute(RunningListAction action);

    void redo();

    boolean canRedo();

    boolean canUndo();

    void undo();

    void clear();
}
