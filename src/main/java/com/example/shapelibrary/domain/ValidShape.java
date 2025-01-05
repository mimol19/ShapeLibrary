package com.example.shapelibrary.domain;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ShapeValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidShape {
    String message() default "Invalid shape configuration";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}