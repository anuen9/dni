package org.anuen.patient.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "default-info")
public class DefaultInfoProperties {
    private String password;
    private Integer userType;
}
