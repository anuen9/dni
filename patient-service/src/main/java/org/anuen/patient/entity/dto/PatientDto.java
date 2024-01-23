package org.anuen.patient.entity.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PatientDto {
    private String firstName;
    private String lastName;
    private Integer age;
    private Integer gender;
    private String email;
    private String phone;
}
