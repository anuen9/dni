package org.anuen.email.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true, fluent = true)
public class EmailSettings {
    @NotBlank
    private String destination;

    @NotBlank
    private String subject;

    public static EmailSettings newSettings() {
        return new EmailSettings();
    }
}
