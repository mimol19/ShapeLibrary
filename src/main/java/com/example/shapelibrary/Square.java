package com.example.shapelibrary;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Square implements Shape{
    private double side;


    @Override
    public String getType() {
        return "SQUARE";
    }

    @Override
    public double calculateArea() {
        return side*side;
    }
}
