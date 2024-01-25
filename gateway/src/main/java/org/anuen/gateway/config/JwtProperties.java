package org.anuen.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

import java.time.Duration;

@Data
@ConfigurationProperties(prefix = "anuen.jwt")
public class JwtProperties {
    private Resource location;
    private String algorithm;
    private String password;
    private String alias;
    private Duration tokenTTL;
}
