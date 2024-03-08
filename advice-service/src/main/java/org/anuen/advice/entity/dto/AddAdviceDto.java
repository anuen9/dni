package org.anuen.advice.entity.dto;

import lombok.Data;

@Data
public class AddAdviceDto {
    private String patientUid;
    private String doctorUid;
    private String content;
    private Integer needNursing;
    private Integer requiredNursingNumber;
    private Integer nursingFrequency;
    private String status;
    private Integer appointmentId;
}
