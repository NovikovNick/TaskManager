package com.metalheart.model.request;

import com.metalheart.validation.constraint.ExistEmail;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StartChangePasswordPasswordRequest {

    @NotNull
    @NotEmpty
    @Email
    @ExistEmail
    private String email;
}
