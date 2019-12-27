package com.metalheart;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Data
@Component
@ConfigurationProperties(prefix = "taskmanager.security")
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class AppProperties {

    @NotNull
    private String defaultUsername;

    @NotNull
    private String defaultEmail;

    @NotNull
    private String defaultPassword;

}
