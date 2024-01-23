package org.anuen.common.enums;

import lombok.Getter;

@Getter
public enum ResponseStatus {
    SUCCESS(0, "success"),
    FAIL(-1, "fail"),
    USER_EXISTS(1001, "user exists"),

    ;
    private final Integer code;
    private final String message;

    ResponseStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
