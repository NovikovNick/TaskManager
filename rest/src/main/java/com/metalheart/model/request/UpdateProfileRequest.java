package com.metalheart.model.request;

import com.metalheart.model.response.TagViewModel;
import java.util.List;
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
    private List<TagViewModel> tags;
}
