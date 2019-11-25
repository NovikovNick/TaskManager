package com.metalheart.model;

public interface RunningListAction<T> {

    T execute();

    void undo();

}
