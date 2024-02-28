package org.anuen.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "anuen.default-info")
public class DefaultInfoProperties {
    private String password;
    private Integer patientUserType;
    private Integer nurseUserType;
    private Integer doctorUserType;
}
