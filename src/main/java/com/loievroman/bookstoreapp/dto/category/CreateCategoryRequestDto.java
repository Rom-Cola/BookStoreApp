package com.loievroman.bookstoreapp.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class CreateCategoryRequestDto {
    @NotBlank
    private String name;
    private String description;
}
