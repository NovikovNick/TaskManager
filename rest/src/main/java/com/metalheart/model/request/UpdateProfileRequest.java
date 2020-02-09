package com.metalheart.model.request;

import com.metalheart.model.response.TagViewModel;
import com.metalheart.validation.constraint.UniqueEmail;
import java.util.List;
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
public class UpdateProfileRequest {

    @NotNull
    @NotEmpty
    private String username;

    @NotNull
    @NotEmpty
    @Email
    @UniqueEmail
    private String email;

    @NotNull
    private List<TagViewModel> tags;
}
