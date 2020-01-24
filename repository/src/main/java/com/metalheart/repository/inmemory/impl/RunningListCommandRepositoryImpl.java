package com.metalheart.repository.inmemory.impl;

import com.metalheart.model.RunningListAction;
import com.metalheart.model.RunningListCommandDeque;
import com.metalheart.repository.inmemory.RunningListCommandRepository;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class RunningListCommandRepositoryImpl implements RunningListCommandRepository {

    private Map<Integer, RunningListCommandDeque> commandsDequeByUser = new HashMap<>();

    @Override
    public void addAction(Integer userId, RunningListAction action) {
        getUserCommandDeque(userId).addAction(action);
    }

    @Override
    public RunningListAction popDone(Integer userId) {
        return getUserCommandDeque(userId).popDone();
    }

    @Override
    public void pushUndone(Integer userId, RunningListAction action) {
        getUserCommandDeque(userId).pushUndone(action);
    }

    @Override
    public RunningListAction popUndone(Integer userId) {
        return getUserCommandDeque(userId).popUndone();
    }

    @Override
    public void pushDone(Integer userId, RunningListAction action) {
        getUserCommandDeque(userId).pushDone(action);
    }

    @Override
    public boolean hasDone(Integer userId) {
        return getUserCommandDeque(userId).hasDone();
    }

    @Override
    public boolean hasUndone(Integer userId) {
        return getUserCommandDeque(userId).hasUndone();
    }

    @Override
    public void clear(Integer userId) {
        getUserCommandDeque(userId).clear();
    }

    private RunningListCommandDeque getUserCommandDeque(Integer userId) {
        if (!commandsDequeByUser.containsKey(userId)) {
            commandsDequeByUser.put(userId, new RunningListCommandDeque());
        }
        return commandsDequeByUser.get(userId);
    }
}
