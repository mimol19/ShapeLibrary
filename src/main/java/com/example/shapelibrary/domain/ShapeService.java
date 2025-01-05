package com.example.shapelibrary.domain;

import com.example.shapelibrary.controller.dto.ShapeDto;
import com.example.shapelibrary.repository.ShapeRepository;
import com.example.shapelibrary.repository.entities.User;
import com.example.shapelibrary.repository.entities.Shape;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ShapeService {
    public final Map<String, Shape> shapeMap;
    private final ShapeRepository shapeRepository;
    private final UserService userService;
    private final ShapeMapper shapeMapper;

    @Autowired
    public ShapeService(List<Shape> shapes, ShapeRepository shapeRepository,
                        UserService userService, ShapeMapper shapeMapper) {
        this.shapeMap = shapes.stream().collect(Collectors.toMap(Shape::getType, shape -> shape));
        this.shapeRepository = shapeRepository;
        this.userService = userService;
        this.shapeMapper = shapeMapper;
    }

    @Transactional
    public ShapeDto createShape(ShapeDto shapeDto) {
        String type = shapeDto.getType();

        Shape shape = Optional.ofNullable(shapeMap.get(type))
                .orElseThrow(() -> new IllegalArgumentException("Unknown shape type: " + type));

        User user = userService.getOrCreateUser(shapeDto.getUserName());
        shape.setUser(user);

        shapeMapper.mapToShape(shapeDto, shape);
        Shape saved = getSaved(shape);
        log.info("Shape created and saved: ID={}, Type={}, Parameters={}, User={}",
                saved.getId(), saved.getType(), saved.getParameters(), saved.getUser().getName());

        return shapeMapper.mapToDto(saved);
    }

    public Shape getSaved(@Valid Shape shape) {
        return shapeRepository.save(shape);
    }

    public List<ShapeDto> getShapesByType(String type) {

        Optional.ofNullable(shapeMap.get(type))
                .orElseThrow(() -> new IllegalArgumentException("Unknown shape type: " + type));

        List<Shape> byType = shapeRepository.findByType(type);
        log.info("Retrieved {} shapes of type: {}", byType.size(), type);
        return byType.stream().map(shapeMapper::mapToDto).collect(Collectors.toList());
    }

    @Transactional
    public ShapeDto changeCircleRadiusToTen(Long id) {
        Shape shape = shapeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Shape not found with ID: " + id));

        if (!"CIRCLE".equals(shape.getType())) {
            throw new IllegalArgumentException("Only circles can have their radius changed.");
        }

        shape.setParameters(new double[]{10.0});
        log.info("Circle radius changed: ID={}, New Radius={}, User={}",
                shape.getId(), shape.getParameters()[0], shape.getUser().getName());
        return shapeMapper.mapToDto(shape);
    }
}
