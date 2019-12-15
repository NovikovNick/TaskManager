package com.metalheart.model.rest.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TagViewModel implements Cloneable {

    private String id;

    private String text;

    @Override
    public TagViewModel clone() {
        return TagViewModel.builder()
            .id(id)
            .text(text)
            .build();
    }
}