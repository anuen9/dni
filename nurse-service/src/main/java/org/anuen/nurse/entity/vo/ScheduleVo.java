package org.anuen.nurse.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalTime;
import java.util.Date;

@Data
public class ScheduleVo {
    private Integer scheduleId;
    private String nurseName;
    private String shiftName;
    private String shiftType;
    private LocalTime startTime;
    private LocalTime endTime;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date date;

}
