package com.metalheart.config;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Data
@Component
@ConfigurationProperties(prefix = "taskmanager")
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class AppProperties {

    @Valid
    private Security security = new Security();

    @Valid
    private Rest rest = new Rest();

    @Getter
    @Setter
    public static class Rest {

        @NotNull
        private Integer maxPayloadLength;

        private boolean includeRequest;

        private boolean includeRequestHeaders;

        private boolean includeResponse;

        private boolean includeResponseHeaders;

        @NotNull
        private String frontUrl;

        @NotNull
        private String backUrl;
    }

    @Getter
    @Setter
    public static class Security {

        @NotNull
        private String defaultUsername;

        @NotNull
        private String defaultEmail;

        @NotNull
        private String defaultPassword;
    }
}
