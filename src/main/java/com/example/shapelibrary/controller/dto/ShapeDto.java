package com.example.shapelibrary.controller.dto;

import jakarta.validation.constraints.*;
import lombok.*;


@AllArgsConstructor
@Getter
@Builder
@Value
public class ShapeDto {
    @Positive
    @Null(message = "id must be null")
    Long id;
    @NotBlank(message = "Shape type cannot be empty or null")
    String type;
    @NotNull(message = "Parameters cannot be null")
    @Size(min = 1, message = "At least one parameter is required.")
    double[] parameters;
    @NotBlank(message = "User name cannot be blank")
    String userName;
}
