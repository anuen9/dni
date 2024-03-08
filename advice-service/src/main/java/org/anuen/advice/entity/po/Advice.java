package org.anuen.advice.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("advice")
public class Advice {
    @TableId(type = IdType.AUTO)
    private Integer adviceId;
    private String patientUid;
    private String doctorUid;
    private String content;
    private Integer needNursing;
    private Integer requiredNursingNumber;
    private Integer nursingFrequency;
    private String status;
    private Integer appointmentId;
    private Date createdTime;
    private Date updatedTime;

    public static Advice newInstance() {
        return new Advice();
    }

}
