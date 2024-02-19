package org.anuen.email.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Data
@ConfigurationProperties(prefix = "anuen.email")
public class MailProperties {

    private String resourcePath;

    private Map<String, String> templateName;

    private String sourceAddr;
}
