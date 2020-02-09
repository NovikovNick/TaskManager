package com.metalheart;

public class EndPoint {

    public static final String CREATE_TASK = "taskmanager/task";
    public static final String UPDATE_TASK = "taskmanager/task";
    public static final String DELETE_TASK = "taskmanager/task/{taskId}";
    public static final String CHANGE_TASK_STATUS = "taskmanager/task/status";
    public static final String CHANGE_TASK_PRIORITY = "taskmanager/task/priority";


    public static final String ADD_TAG_TO_TASK = "taskmanager/tag/task";
    public static final String REMOVE_TAG_FROM_TASK = "taskmanager/tag/task";

    public static final String ADD_TASK_TAG = "taskmanager/tag";
    public static final String REMOVE_TASK_TAG = "taskmanager/tag";
    public static final String GET_TASK_TAG_LIST = "taskmanager/tag/list";

    public static final String RUNNING_LIST_ARCHIVE = "taskmanager/runninglist/archive";
    public static final String RUNNING_LIST_ARCHIVE_NEXT = "taskmanager/runninglist/archive/next";
    public static final String RUNNING_LIST_ARCHIVE_PREV = "taskmanager/runninglist/archive/prev";
    public static final String RUNNING_LIST_UNDO = "taskmanager/runninglist";
    public static final String RUNNING_LIST_REDO = "taskmanager/runninglist";
    public static final String RUNNING_LIST = "taskmanager/runninglist";

    public static final String AUTH_SIGN_IN = "/auth/signin";
    public static final String AUTH_SIGN_OUT = "/auth/signout";

    public static final String USER_REGISTRATION = "/user";
    public static final String SAVE_PROFILE = "/profile";

    private EndPoint() {
        throw new UnsupportedOperationException();
    }
}
