package org.anuen.common.enums;

import lombok.Getter;

@Getter
public enum ResponseStatus {
    // fail response: code are start with 100
    FAIL(1000, "fail"),
    USER_EXISTS(1001, "user exists"),
    LOGIN_FORM_EMPTY(1002, "account or password can't be empty"),
    PERMISSION_DENY(1003, "illegal access credentials (invalid password)"),
    PARAM_LOSE(1004, "the necessary parameters are missing"),
    REMOTE_PROCEDURE_CALL_ERROR(1005, "the service invoked remotely failed"),
    USER_NOT_FOUND(1006, "user not found"),
    EMAIL_NOT_VERIFIED(1007, "e-mailbox not verified, verify your e-mailbox to continue"),
    IS_NOT_EMAIL_ADDRESS(1008, ExceptionMessage.IS_NOT_EMAIL_ADDRESS.getMessage()),
    NECESSARY_PARAM_MISSING(1009, "necessary parameter missing"),

    // normal response
    UNAUTHORIZED(401, "no authorized"),
    SUCCESS(200, "success"),
    ;
    private final Integer code;
    private final String message;

    ResponseStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
