package com.example.shapelibrary;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ShapeFactory {
    private static final Map<String, Function<double[], Shape>> SHAPES_MAP = new HashMap<>();

    static  {
        SHAPES_MAP.put("SQUARE", parameters -> new Square(parameters[0]));
        SHAPES_MAP.put("CIRCLE", parameters -> new Circle(parameters[0]));
        SHAPES_MAP.put("RECTANGLE", parameters -> new Rectangle(parameters[0], parameters[1]));
    }
    public static Shape createShape(String type, double[] parameters) {
        Function<double[], Shape> shapeCreator = SHAPES_MAP.get(type.toUpperCase());
        if (shapeCreator == null) {
            throw new IllegalArgumentException("Unknown shape type: " + type);
        }
        return shapeCreator.apply(parameters);
    }

}
