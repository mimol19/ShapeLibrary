package com.example.shapelibrary.domain;

import com.example.shapelibrary.repository.entities.Shape;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ShapeValidator implements ConstraintValidator<ValidShape, Shape> {

    @Override
    public boolean isValid(Shape shape, ConstraintValidatorContext context) {
        if (shape == null) {
            return true;
        }

        double[] params = shape.getParameters();
        if (params == null) {
            return false;
        }

        return hasCorrectParameterCount(shape, params)
                && areAllParametersPositive(params);
    }

    private boolean hasCorrectParameterCount(Shape shape, double[] params) {
        return params.length == shape.getRequiredParameterCount();
    }

    private boolean areAllParametersPositive(double[] params) {
        for (double param : params) {
            if (param <= 0) {
                return false;
            }
        }
        return true;
    }
}
