package com.example.shapelibrary.business;

import com.example.shapelibrary.controller.dto.ShapeDto;
import com.example.shapelibrary.repository.CreatorRepository;
import com.example.shapelibrary.repository.ShapeRepository;
import com.example.shapelibrary.repository.entities.Creator;
import com.example.shapelibrary.repository.entities.Shape;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ShapeService {
    public final Map<String, Shape> shapeMap;
    private final ShapeRepository shapeRepository;
    private final CreatorRepository creatorRepository;

    @Autowired
    public ShapeService(List<Shape> shapes, ShapeRepository shapeRepository, CreatorRepository creatorRepository) {
        this.shapeMap = shapes.stream().collect(Collectors.toMap(Shape::getType, shape -> shape));
        this.shapeRepository = shapeRepository;
        this.creatorRepository = creatorRepository;
    }

    @Transactional
    public ShapeDto createShape(ShapeDto shapeDto) {
        String type = shapeDto.getType();

        Shape shape = shapeMap.get(type);
        if (shape == null) {
            throw new IllegalArgumentException("Unknown shape type: " + type);
        }

        Creator creator = getOrCreateCreator(shapeDto.getCreatorName());
        shape.setCreator(creator);

        mapToShape(shapeDto, shape);
        Shape saved = shapeRepository.save(shape);
        return mapToDto(saved);
    }

    public List<ShapeDto> getShapesByType(String type) {
        List<Shape> byType = shapeRepository.findByType(type);
        return byType.stream().map(ShapeService::mapToDto).collect(Collectors.toList());
    }

    @Transactional
    public ShapeDto changeCircleRadiusToTen(Long id) {
        Optional<Shape> byId = shapeRepository.findById(id);
        if (byId.isPresent()) {
            if (byId.get().getType().equals("CIRCLE")) {
                byId.get().setParameters(new double[]{10});
                return mapToDto(byId.get());
            }
        }
        return null;
    }

    public Creator getOrCreateCreator(String creatorName) {
        return creatorRepository.findByName(creatorName)
                .orElseGet(() -> {
                    return creatorRepository.save(Creator.builder().name(creatorName).build());
                });
    }

    public static ShapeDto mapToDto(Shape shape) {
        return ShapeDto.builder().
                id(shape.getId())
                .type(shape.getType())
                .creatorName(shape.getCreator().getName())
                .parameters(shape.getParameters())
                .build();
    }

    public static void mapToShape(ShapeDto shapeDto, Shape shape) {
        shape.setParameters(shapeDto.getParameters());
        shape.setType(shapeDto.getType());
        shape.setId(null);
    }

}
