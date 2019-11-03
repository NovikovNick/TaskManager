package com.metalheart;

public class EndPoint {


    public static final String GET_TASK_LIST = "taskmanager/task/list";
    public static final String CREATE_TASK = "taskmanager/task";
    public static final String UPDATE_TASK = "taskmanager/task";
    public static final String DELETE_TASK = "taskmanager/task/{taskId}";
    public static final String CHANGE_TASK_STATUS = "taskmanager/task/status";
    public static final String CHANGE_TASK_PRIORITY = "taskmanager/task/priority";

    private EndPoint() {
        throw new UnsupportedOperationException();
    }
}
