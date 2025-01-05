package com.example.shapelibrary.domain;

import com.example.shapelibrary.controller.dto.ShapeDto;
import com.example.shapelibrary.repository.entities.Shape;
import org.springframework.stereotype.Component;

@Component
public class ShapeMapper {

    public ShapeDto mapToDto(Shape shape) {
        return ShapeDto.builder().
                id(shape.getId())
                .type(shape.getType())
                .userName(shape.getUser().getName())
                .parameters(shape.getParameters())
                .build();
    }

    public void mapToShape(ShapeDto shapeDto, Shape shape) {
        shape.setParameters(shapeDto.getParameters());
        shape.setType(shapeDto.getType());
        shape.setId(null);
    }

}
