package org.anuen.doctor.entity.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddDoctorDto {
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String specialty;

    @NotBlank
    private String phone;

    @NotBlank
    private String email;

    private String address;

    private String otherDetails;
}
