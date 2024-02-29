package org.anuen.utils;

import org.anuen.common.constraint.SysUser;
import org.anuen.common.entity.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class SysUserUtil {


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
                .password("123456") /* default password */
                .build();
    }
}
