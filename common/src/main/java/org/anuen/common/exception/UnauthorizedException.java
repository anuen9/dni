package org.anuen.common.exception;

public class UnauthorizedException extends CommonException {

    public UnauthorizedException(String message) {
        super(message, 401);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause, 401);
    }
}
