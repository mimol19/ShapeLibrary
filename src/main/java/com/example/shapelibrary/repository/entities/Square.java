package com.example.shapelibrary.repository.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import org.springframework.stereotype.Component;

@Component
@Entity
@DiscriminatorValue("square")
public class Square extends Shape {
    private static final int REQUIRED_PARAMS = 1;
    @Override
    public String getType() {
        return "SQUARE";
    }


    @Override
    public double calculateArea() {
        return parameters[0] * parameters[0];
    }

    @Override
    public int getRequiredParameterCount() {
        return REQUIRED_PARAMS;
    }
}
