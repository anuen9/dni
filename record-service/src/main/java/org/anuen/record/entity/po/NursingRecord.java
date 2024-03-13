package org.anuen.record.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("nursing_record")
public class NursingRecord {
    @TableId(type = IdType.AUTO)
    private Integer nursingRecordId;
    private Integer adviceId;
    private Date nursingDateStart;
    private Date nursingDateEnd;
    private Integer currentNursingNumber;
    private String nursingBy;
    private String patientUid;
    private Integer hasBadReaction;
    private Date createTime;

    public static NursingRecord newInstance() {
        return new NursingRecord();
    }
}
