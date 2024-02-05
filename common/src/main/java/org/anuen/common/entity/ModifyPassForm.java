package org.anuen.common.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ModifyPassForm {
    private String userUid;

    @NotBlank(message = "[old password] is required")
    private String oldPassword;

    @NotBlank(message = "[new password] is required")
    private String newPassword;
}
