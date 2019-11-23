package com.metalheart;

public class EndPoint {

    public static final String CREATE_TASK = "taskmanager/task";
    public static final String UPDATE_TASK = "taskmanager/task";
    public static final String DELETE_TASK = "taskmanager/task/{taskId}";
    public static final String CHANGE_TASK_STATUS = "taskmanager/task/status";
    public static final String CHANGE_TASK_PRIORITY = "taskmanager/task/priority";

    public static final String RUNNING_LIST_ARCHIVE = "taskmanager/runninglist/archive";
    public static final String RUNNING_LIST_ARCHIVE_NEXT = "taskmanager/runninglist/archive/next";
    public static final String RUNNING_LIST_ARCHIVE_PREV = "taskmanager/runninglist/archive/prev";
    public static final String RUNNING_LIST_UNDO = "taskmanager/runninglist";
    public static final String RUNNING_LIST_REDO = "taskmanager/runninglist";
    public static final String RUNNING_LIST = "taskmanager/runninglist";

    private EndPoint() {
        throw new UnsupportedOperationException();
    }
}
