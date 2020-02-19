package com.metalheart.model.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FormValidationErrorViewModel {

    private List<ParameterValidationError> errors;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ParameterValidationError {

        private String parameter;
        private String message;
    }
}
