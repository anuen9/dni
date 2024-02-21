package org.anuen.patient.entity.vo;

import lombok.Data;

import java.util.Date;

@Data
public class PatientVo {

    private String uid;

    private String name;

    private Integer age;

    private Integer gender;

    private String email;

    private Integer emailStatus;

    private String phone;

    private Date createTime;

    public static PatientVo newInstance() {
        return new PatientVo();
    }

}
