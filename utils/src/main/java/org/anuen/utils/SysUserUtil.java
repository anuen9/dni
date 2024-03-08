package org.anuen.utils;

import lombok.RequiredArgsConstructor;
import org.anuen.api.client.UserClient;
import org.anuen.common.constraint.SysUser;
import org.anuen.common.entity.ResponseEntity;
import org.anuen.common.entity.dto.UserDto;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class SysUserUtil {

    private final UserClient userClient;

    private final RPCRespResolver respResolver;

    /**
     * you can provide a SysUser(Patient, Doctor, Nurse) into this method
     * to get a UserDto inorder to insert user into User database.
     *
     * @param sysUser sysUser like Patient, Doctor, Nurse
     * @return UserDto
     */
    @NonNull
    public UserDto getUserDto4Save(@NonNull SysUser sysUser) {
        return UserDto.builder()
                .uid(sysUser.getUid())
                .userType(sysUser.getUserType())
                .nickName(sysUser.getNickname())
                .password("123456") /* default password */
                .build();
    }

    /**
     * you can use a user uid to acquire its user type.
     *
     * @param uid user uid
     * @return user type: 0 patient / 1 nurse / 2 doctor.
     * the return value of -1 means remote invoke was failure。
     */
    @NonNull
    public Integer getUserType(@NonNull String uid) {
        ResponseEntity<?> response = userClient.getUserTypeByUid(uid);
        Integer userType = respResolver.getRespData(response, Integer.class);
        if (Objects.isNull(userType)) {
            return -1;
        }
        return userType;
    }
}
