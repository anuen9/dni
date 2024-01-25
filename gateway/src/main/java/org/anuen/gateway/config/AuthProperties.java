package org.anuen.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "anuen.auth")
public class AuthProperties {
    private List<String> includePath;
    private List<String> excludePath;
}
