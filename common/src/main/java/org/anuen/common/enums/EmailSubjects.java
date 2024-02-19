package org.anuen.common.enums;

import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public enum EmailSubjects {
    VERIFY_CODE("Verify your email address now!"),

    ;

    private final String description;

    EmailSubjects(String description) {
        this.description = description;
    }
}
