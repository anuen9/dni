package org.anuen.utils;

import org.anuen.common.config.DefaultInfoProperties;
import org.anuen.common.constraint.SysUser;
import org.anuen.common.entity.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SysUserUtil {

    @Autowired
    private DefaultInfoProperties defaultInfoProperties;

    /**
     * you can provide a SysUser(Patient, Doctor, Nurse) into this method
     * to get a UserDto inorder to insert user into User database.
     *
     * @param sysUser sysUser like Patient, Doctor, Nurse
     * @return UserDto
     */
    public UserDto getUserDto4Save(SysUser sysUser) {
        return UserDto.builder()
                .uid(sysUser.getUid())
                .userType(sysUser.getUserType())
                .nickName(sysUser.getNickname())
                .password(defaultInfoProperties.getPassword())
                .build();
    }
}
