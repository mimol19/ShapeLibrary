package com.example.shapelibrary.repository.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
    public void setParameters(double[] doubles) {
        this.width = doubles[0];
        this.length = doubles[1];
    }

    @Override
    public double[] getParameters() {
        return new double[]{width,length};
    }

    @Override
    public double calculateArea() {
        return width * length;
    }


}
