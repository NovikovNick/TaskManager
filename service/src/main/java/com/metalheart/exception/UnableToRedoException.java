package com.metalheart.exception;

public class UnableToRedoException extends Exception {

    public UnableToRedoException() {
        super("There are no undone operations to redo");
    }
}
