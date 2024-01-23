package org.anuen.patient.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * 用户信息表
 */
@Data
@Builder
@TableName("patient")
public class Patient {
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户唯一id
     */
    private String uid;

    /**
     * 患者的姓氏
     */
    private String firstName;

    /**
     * 患者的名
     */
    private String lastName;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 邮箱状态 0-未验证 1-已验证
     */
    private Integer emailStatus;

    /**
     * 电话号码
     */
    private String phone;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    public static Patient newInstance() {
        return Patient.builder().build();
    }

}
