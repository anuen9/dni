package org.anuen.common.enums;

import lombok.Getter;

@Getter
public enum ResponseStatus {
    // success response: code is 0
    SUCCESS(0, "success"),
    USER_NOT_FOUND(0, "user not found"),

    // fail response: code are start with 100
    FAIL(1000, "fail"),
    USER_EXISTS(1001, "user exists"),

    ;
    private final Integer code;
    private final String message;

    ResponseStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
