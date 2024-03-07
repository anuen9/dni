package org.anuen.appointment.entity.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ModifyApptDto {
    private Integer appointmentId;

    private String diagnosis;

    private String prescription;

    private String notes;

    private BigDecimal appointmentFee;

    private String allergies;

    private Integer adviceId;
}
