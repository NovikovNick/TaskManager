package com.metalheart.config;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Data
@Component
@ConfigurationProperties(prefix = "taskmanager.rest")
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class AppRestProperties {

    @NotNull
    private Integer maxPayloadLength;

    private boolean includeRequest;

    private boolean includeRequestHeaders;

    private boolean includeResponse;

    private boolean includeResponseHeaders;
}
