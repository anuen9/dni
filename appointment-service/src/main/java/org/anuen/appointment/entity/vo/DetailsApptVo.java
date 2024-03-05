package org.anuen.appointment.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class DetailsApptVo {
    private Integer appointmentId;
    private String patientName;
    private String doctorName;
    private String nurseName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date appointmentDate;
    private String diagnosis;
    private String prescription;
    private String notes;
    private String appointmentStatus;
    private BigDecimal appointmentFee;
    private String allergies;
    private Integer isEmergency;
    private Integer adviceId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatedTime;

    public static DetailsApptVo newInstance() {
        return new DetailsApptVo();
    }

}
