package com.example.shapelibrary;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ShapeFactory {
    private final Map<String, Shape> shapeMap;
    private final ShapeRepository shapeRepository;

    @Autowired
    public ShapeFactory(List<Shape> shapes, ShapeRepository shapeRepository) {
        this.shapeMap = shapes.stream().collect(Collectors.toMap(Shape::getType, shape -> shape));
        this.shapeRepository = shapeRepository;
    }

    public Shape createShape(ShapeRequest shapeRequest) {
        String type = shapeRequest.getType();

        Shape shape = shapeMap.get(type);
        if (shape == null) {
            throw new IllegalArgumentException("Unknown shape type: " + type);
        }

        shape.setParameters(shapeRequest.getParameters());
        shape.setType(shapeRequest.getType());
        shape.setId(null);

        return shapeRepository.save(shape);
    }

    public List<Shape> getShapesByType(String type) {
        return shapeRepository.findByType(type);
    }

}
