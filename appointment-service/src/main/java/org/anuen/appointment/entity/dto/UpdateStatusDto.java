package org.anuen.appointment.entity.dto;

import lombok.Data;

@Data
public class UpdateStatusDto {
    private Integer appointmentId;
    private String status;
}
