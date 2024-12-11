package com.example.shapelibrary;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import org.springframework.stereotype.Component;

@Component
@Entity
@DiscriminatorValue("square")
public class Square extends Shape{
    private  double side;
@Override
    public void setParameters(double[] doubles) {
        this.side = doubles[0];
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
