package org.anuen.appointment;

import lombok.Getter;

@Getter
public enum AppointmentStatusEnum {
    PROGRESS("progress"),
    ADMITTED("admitted"),
    DISCHARGED("discharged"),

    ;
    private final String status;

    AppointmentStatusEnum(String status) {
        this.status = status;
    }
}
