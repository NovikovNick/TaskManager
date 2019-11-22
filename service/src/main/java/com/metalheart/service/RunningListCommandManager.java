package com.metalheart.service;

import com.metalheart.model.RunningListAction;

public interface RunningListCommandManager {

    void execute(RunningListAction action);

    void redo();

    boolean canRedo();

    void undo();

    boolean canUndo();

    void clear();
}
