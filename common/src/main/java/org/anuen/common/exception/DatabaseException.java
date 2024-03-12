package org.anuen.common.exception;

public class DatabaseException extends CommonException{
    public DatabaseException(String message) {
        super(message, 500);
    }
}
