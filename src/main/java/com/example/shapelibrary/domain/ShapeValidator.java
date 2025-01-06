package com.example.shapelibrary.domain;

import com.example.shapelibrary.repository.entities.Shape;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class ShapeValidator implements ConstraintValidator<ValidShape, Shape> {

    @Override
    public boolean isValid(Shape shape, ConstraintValidatorContext context) {
        if (shape == null) {
            return true;
        }

        List<Double> params = shape.getParameters();
        if (params == null) {
            return false;
        }

        return hasCorrectParameterCount(shape, params)
                && areAllParametersPositive(params);
    }

    private boolean hasCorrectParameterCount(Shape shape, List<Double> params) {
        return params.size() == shape.getRequiredParameterCount();
    }

    private boolean areAllParametersPositive(List<Double> params) {
        for (double param : params) {
            if (param <= 0) {
                return false;
            }
        }
        return true;
    }
}
