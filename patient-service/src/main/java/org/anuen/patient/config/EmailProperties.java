package org.anuen.patient.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "anuen.email")
public class EmailProperties {
    private Integer emailVerified;
    private Integer emailNotVerified;
}
