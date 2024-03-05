package org.anuen.appointment.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class SimpleApptVo {
    private Integer appointmentId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date appointmentDate;
    private String diagnosis;
    private String appointmentStatus;

    private SimpleApptVo() {
    }

    public static SimpleApptVo newInstance() {
        return new SimpleApptVo();
    }
}
