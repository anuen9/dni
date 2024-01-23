package org.anuen.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private String uid;
    private Integer userType;
    private String nickName;
    private String password;
}
