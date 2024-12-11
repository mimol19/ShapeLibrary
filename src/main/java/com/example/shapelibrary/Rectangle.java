package com.example.shapelibrary;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Entity
@DiscriminatorValue("rectangle")
public class Rectangle extends Shape {
    private double width;
    private double length;


    @Override
    public String getType() {
        return "RECTANGLE";
    }

    @Override
    void setParameters(double[] doubles) {
        this.width = doubles[0];
        this.length = doubles[1];
    }

    @Override
    public double calculateArea() {
        return width * length;
    }


}
