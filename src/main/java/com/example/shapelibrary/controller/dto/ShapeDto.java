package com.example.shapelibrary.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;


@AllArgsConstructor
@Getter
@Setter
@Builder
public class ShapeDto {
    private Long id;
    private String type;

    private double[] parameters;
    private String creatorName;
}
