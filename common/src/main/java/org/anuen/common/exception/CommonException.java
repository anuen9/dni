package org.anuen.common.exception;

import lombok.Getter;

@Getter
public class CommonException extends RuntimeException {
    private final Integer code;

    public CommonException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public CommonException(String message, Throwable cause, Integer code) {
        super(message, cause);
        this.code = code;
    }
}
