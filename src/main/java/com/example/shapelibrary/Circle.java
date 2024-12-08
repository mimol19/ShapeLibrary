package com.example.shapelibrary;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
public class Circle implements Shape{
    private double radius;

    @Override
    public String getType() {
        return "CIRCLE";
    }

    @Override
    public double calculateArea() {
        return radius*radius;
    }
}
