package org.anuen.doctor.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.anuen.common.constraint.SysUser;

@Data
@TableName("doctor")
public class Doctor implements SysUser {
    @TableId(type = IdType.AUTO)
    private Integer doctorId;
    private String uid;
    private String firstName;
    private String lastName;
    private String specialty;
    private String phone;
    private String email;
    private String address;
    private String otherDetails;

    private Doctor() {}

    public static Doctor newInstance() {
        return new Doctor();
    }

    @Override
    public String getNickname() {
        return lastName + firstName;
    }

    @Override
    public Integer getUserType() {
        return 2;
    }
}
