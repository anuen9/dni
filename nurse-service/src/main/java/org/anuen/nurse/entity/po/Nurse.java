package org.anuen.nurse.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.anuen.common.constraint.SysUser;

@Data
@TableName("nurse")
public class Nurse implements SysUser {
    @TableId(type = IdType.AUTO)
    private Integer nurseId;

    private String uid;

    private String firstName;

    private String lastName;

    private Integer age;

    private Integer gender;

    private String email;

    private String phone;

    private Integer isLeader;

    private Integer leaderId;

    private Integer isBusy;

    public static Nurse newInstance() {
        return new Nurse();
    }

    @Override
    public String getNickname() {
        return lastName + firstName;
    }

    @Override
    public Integer getUserType() {
        return 1;
    }
}
