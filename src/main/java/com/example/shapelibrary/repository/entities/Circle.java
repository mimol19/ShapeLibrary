package com.example.shapelibrary.repository.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import org.springframework.stereotype.Component;

@Component
@Entity
@DiscriminatorValue("circle")
public class Circle extends Shape {
    private double radius;


    @Override
    public String getType() {
        return "CIRCLE";
    }

    @Override
    public void setParameters(double[] doubles) {
        this.radius = doubles[0];
    }

    @Override
    public double[] getParameters() {
        return new double[]{radius};
    }


    @Override
    public double calculateArea() {
        return radius * radius;
    }
}
