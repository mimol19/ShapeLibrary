package com.example.shapelibrary;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShapeRequest {
    private String type;
    private double[] parameters;
}
