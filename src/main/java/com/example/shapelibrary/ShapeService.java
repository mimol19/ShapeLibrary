package com.example.shapelibrary;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class ShapeService {
    public ShapeRepository shapeRepository;

    public ShapeEntity addShape(String type, double[] parameters) {
        Shape shape = ShapeFactory.createShape(type, parameters);

        ShapeEntity shapeEntity = new ShapeEntity();
        shapeEntity.setType(shape.getType());
        List<Double> parameterList = new ArrayList<>();
        for (double parameter : parameters) {
            parameterList.add(parameter);
        }
        shapeEntity.setParameters(parameterList);
        return shapeRepository.save(shapeEntity);
    }

    public List<ShapeEntity> getShapesByType(String type) {
        return shapeRepository.findByType(type);
    }
}
