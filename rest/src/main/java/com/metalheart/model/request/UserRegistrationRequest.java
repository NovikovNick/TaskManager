package com.metalheart.model.request;

import com.metalheart.validation.constraint.ConfirmedPassword;
import com.metalheart.validation.constraint.UniqueEmail;
import com.metalheart.validation.constraint.UniqueLogin;
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
@ConfirmedPassword
public class UserRegistrationRequest implements PasswordAware{

    @NotNull
    @NotEmpty
    @UniqueLogin
    private String username;

    @NotNull
    @NotEmpty
    @Email
    @UniqueEmail
    private String email;

    @NotNull
    @NotEmpty
    private String password;

    @NotNull
    @NotEmpty
    private String confirmPassword;
}
