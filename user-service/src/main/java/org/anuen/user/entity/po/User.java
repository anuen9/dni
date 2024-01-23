package org.anuen.user.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * 用户表
 */
@Data
@Builder
@TableName("user")
public class User {
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户唯一id
     */
    private String uid;

    /**
     * 用户类型 0-患者 1-护士 2-医生
     */
    private Integer userType;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 密码
     */
    private String password;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    public static User newInstance() {
        return User.builder().build();
    }
}
