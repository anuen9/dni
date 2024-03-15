package org.anuen.nurse.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalTime;

@Data
@TableName("shift")
public class Shift {
    @TableId(type = IdType.AUTO)
    private Integer shiftId;
    private String name;
    private String type;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer needCount;
}
