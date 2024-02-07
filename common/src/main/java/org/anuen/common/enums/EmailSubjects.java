package org.anuen.common.enums;

import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public enum EmailSubjects {
    VERIFY_CODE("verifyCode"),

    ;

    private final String toString;

    EmailSubjects(String subject) {
        this.toString = subject;
    }
}
