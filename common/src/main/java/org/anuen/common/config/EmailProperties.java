package org.anuen.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "anuen.email")
public class EmailProperties {
    private Integer emailVerified;
    private Integer emailNotVerified;
}