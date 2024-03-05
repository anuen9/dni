package org.anuen.appointment.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("allergies")
public class Allergies {
    @TableId(type = IdType.AUTO)
    private Integer allergiesId;
    private String name;
    private Integer parentId;
}
