package com.example.shapelibrary.repository.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
@AllArgsConstructor
@Builder
@Component
@Entity
@DiscriminatorValue("rectangle")
public class Rectangle extends Shape {
    private static final int REQUIRED_PARAMS = 2;
    @Override
    public String getType() {
        return "RECTANGLE";
    }

    @Override
    public double calculateArea() {
        return parameters.get(0) * parameters.get(1);
    }

    @Override
    public int getRequiredParameterCount() {
        return REQUIRED_PARAMS;
    }
}
