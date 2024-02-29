package org.anuen.nurse.entity.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddNurseDto {

    private String firstName;

    private String lastName;

    private Integer age;

    private Integer gender;

    private String email;

    private String phone;

    private Integer isLeader;

    private Integer leaderId;
}
