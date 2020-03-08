package com.metalheart;

public class EndPoint {

    public static final String CREATE_TASK = "/task";
    public static final String UPDATE_TASK = "/task";
    public static final String DELETE_TASK = "/task/{taskId}";
    public static final String CHANGE_TASK_STATUS = "/task/status";
    public static final String CHANGE_TASK_PRIORITY = "/task/priority";

    public static final String ADD_TAG_TO_TASK = "/tag/task";
    public static final String REMOVE_TAG_FROM_TASK = "/tag/task";

    public static final String ADD_TASK_TAG = "/tag";
    public static final String REMOVE_TASK_TAG = "/tag";
    public static final String GET_TASK_TAG_LIST = "/tag/list";

    public static final String RUNNING_LIST_ARCHIVE = "/archive";
    public static final String RUNNING_LIST_ARCHIVE_NEXT = "/archive/next";
    public static final String RUNNING_LIST_ARCHIVE_PREV = "/archive/prev";
    public static final String RUNNING_LIST_UNDO = "/undo";
    public static final String RUNNING_LIST_REDO = "/redo";
    public static final String RUNNING_LIST = "/runninglist";
    public static final String RUNNING_LIST_DATA = "/runninglist/data";

    public static final String AUTH_SIGN_IN = "/login";
    public static final String AUTH_SIGN_OUT = "/logout";

    public static final String USER_REGISTRATION = "/user";
    public static final String SAVE_PROFILE = "/profile";

    public static final String CHANGE_PASSWORD = "/user/password";
    public static final String SEND_CHANGE_PASSWORD_EMAIL = "/password";


    private EndPoint() {
        throw new UnsupportedOperationException();
    }
}
