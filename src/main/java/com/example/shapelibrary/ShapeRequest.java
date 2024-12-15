package com.example.shapelibrary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ShapeRequest {
    private String type;
    private double[] parameters;
}
