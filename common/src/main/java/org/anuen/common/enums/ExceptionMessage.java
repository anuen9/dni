package org.anuen.common.enums;

import lombok.Getter;

@Getter
public enum ExceptionMessage {
    INVALID_TOKEN("invalid token"),
    TOKEN_EXPIRED("this token was expired"),
    UNAUTHORIZED("unauthorized access"),
    IS_NOT_EMAIL_ADDRESS("param String is not a email address"),


    ;

    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }
}
