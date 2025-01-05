package com.example.shapelibrary.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;


@AllArgsConstructor
@Getter
@Setter
@Builder
@Value
public class ShapeDto {
    Long id;
    String type;
    double[] parameters;
    String userName;
}
