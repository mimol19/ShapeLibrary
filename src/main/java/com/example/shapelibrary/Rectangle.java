package com.example.shapelibrary;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Rectangle implements Shape {
    private double width;
    private double length;


    @Override
    public String getType() {
        return "RECTANGLE";
    }

    @Override
    public double calculateArea() {
        return width*length;
    }
}
