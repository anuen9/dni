package org.anuen.appointment.entity.vo;

import lombok.Data;

import java.util.Date;

@Data
public class SimpleApptVo {
    private Integer appointmentId;
    private Date appointmentDate;
    private String diagnosis;
    private String appointmentStatus;

    private SimpleApptVo() {
    }

    public static SimpleApptVo newInstance() {
        return new SimpleApptVo();
    }
}
