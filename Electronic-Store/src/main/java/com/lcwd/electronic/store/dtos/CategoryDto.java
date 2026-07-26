package com.lcwd.electronic.store.dtos;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {
    private String categoryId;
    @NotNull
    @Min(value = 4,message = "Title must be at least 4 characters long")
    private String title;
    @NotNull(message = "Description must not be null")
    private String description;
    private String coverImage;
}
