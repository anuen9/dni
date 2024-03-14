package org.anuen.advice.enums;

import lombok.Getter;

@Getter
public enum StatusEnum {
    EXECUTING("Executing..."),
    COMPLETED("Completed!"),
    ;
    private final String status;

    StatusEnum(String status) {
        this.status = status;
    }
}
