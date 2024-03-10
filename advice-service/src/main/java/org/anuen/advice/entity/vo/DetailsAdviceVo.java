package org.anuen.advice.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class DetailsAdviceVo {
    private Integer adviceId;
    private String patientName; //
    private String doctorName; //
    private String content;
    private String needNursing; //
    private Integer requiredNursingNumber;
    private String nursingFrequency; //
    private String status;
    private Integer appointmentId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatedTime;

    public static DetailsAdviceVo newInstance() {
        return new DetailsAdviceVo();
    }
}
