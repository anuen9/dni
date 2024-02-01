package org.anuen.common.enums;

import lombok.Getter;

@Getter
public enum ResponseStatus {
    // success response: code is 0
    SUCCESS(0, "success"),

    // fail response: code are start with 100
    FAIL(1000, "fail"),
    USER_EXISTS(1001, "user exists"),
    LOGIN_FORM_EMPTY(1002, "account or password can't be empty"),
    PERMISSION_DENY(1003, "illegal access credentials (invalid password)"),
    PARAM_LOSE(1004, "the necessary parameters are missing"),
    REMOTE_PROCEDURE_CALL_ERROR(1005, "the service invoked remotely failed"),
    USER_NOT_FOUND(1006, "user not found"),

    ;
    private final Integer code;
    private final String message;

    ResponseStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
