package org.anuen.appointment.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@Accessors(chain = true)
@TableName("appointment")
public class Appointment {
    @TableId(type = IdType.AUTO)
    private Integer appointmentId;
    private String patientUid;
    private String doctorUid;
    private String nurseUid;
    private Date appointmentDate;
    private String diagnosis;
    private String prescription;
    private String notes;
    private String appointmentStatus;
    private BigDecimal appointmentFee;
    private String allergies;
    private Integer isEmergency;
    private Integer adviceId;
    private Date createdTime;
    private Date updatedTime;

    private Appointment() {

    }

    public static Appointment newInstance() {
        return new Appointment();
    }
}
