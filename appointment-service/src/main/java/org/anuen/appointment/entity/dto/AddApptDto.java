package org.anuen.appointment.entity.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddApptDto {
    @NotBlank
    private String patientUid;

    @NotBlank
    private String nurseUid;

    @NotBlank
    private String diagnosis;

    @NotBlank
    private String prescription;

    private String notes;

    @NotBlank
    private String appointmentStatus;

    private BigDecimal appointmentFee;

    @NotBlank
    private String allergies;

    private Integer isEmergency;

    private Integer adviceId;
}
