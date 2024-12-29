package com.example.shapelibrary.repository.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import org.springframework.stereotype.Component;

@Component
@Entity
@DiscriminatorValue("square")
public class Square extends Shape {
    private  double side;
@Override
    public void setParameters(double[] doubles) {
        this.side = doubles[0];
    }

    @Override
    public double[] getParameters() {
        return new double[]{side};
    }

    @Override
    public String getType() {
        return "SQUARE";
    }


    @Override
    public double calculateArea() {
        return side*side;
    }
}
