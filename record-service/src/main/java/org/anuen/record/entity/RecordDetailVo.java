package org.anuen.record.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class RecordDetailVo {
    private Integer recordId;
    private Integer adviceId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+*")
    private Date startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+*")
    private Date endTime;
    private Integer thisNum;
    private String nursingBy;
    private String patientName;
    private Boolean badReaction;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+*")
    private Date createTime;
}
