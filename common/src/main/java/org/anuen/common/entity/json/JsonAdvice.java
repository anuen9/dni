package org.anuen.common.entity.json;

import lombok.Data;

import java.util.Date;

@Data
public class JsonAdvice {
    private Integer adviceId;
    private String patientUid;
    private String doctorUid;
    private String content;
    private Integer needNursing;
    private Integer requiredNursingNumber;
    private Integer nursingFrequency;
    private String status;
    private Integer appointmentId;
    private Date createdTime;
    private Date updatedTime;
}
