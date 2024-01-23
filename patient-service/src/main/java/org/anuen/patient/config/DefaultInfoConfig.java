package org.anuen.patient.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DefaultInfoConfig {
    @Value("${patient-default-info.password}")
    private String PASSWORD;

    @Value("${patient-default-info.user-type}")
    private Integer USER_TYPE;

    public String defaultPassword() {
        return PASSWORD;
    }

    public Integer defaultUserType() {
        return USER_TYPE;
    }
}
