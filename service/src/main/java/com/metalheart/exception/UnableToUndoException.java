package com.metalheart.exception;

public class UnableToUndoException extends Exception {

    public UnableToUndoException() {
        super("There are no previous operations to undo");
    }
}
