package org.anuen.common.enums;

import lombok.Getter;

@Getter
public enum EmailStatus {
    EMAIL_NOT_VERIFY(0),
    EMAIL_HAS_VERIFIED(1)

    ;

    private final Integer statusCode;

    EmailStatus(Integer statusCode) {
        this.statusCode = statusCode;
    }
}
