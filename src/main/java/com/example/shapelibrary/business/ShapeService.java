package com.example.shapelibrary.business;

import com.example.shapelibrary.controller.dto.ShapeDto;
import com.example.shapelibrary.repository.ShapeRepository;
import com.example.shapelibrary.repository.entities.User;
import com.example.shapelibrary.repository.entities.Shape;
import jakarta.persistence.EntityNotFoundException;
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
        Shape saved = shapeRepository.save(shape);
        return shapeMapper.mapToDto(saved);
    }

    public List<ShapeDto> getShapesByType(String type) {

        Optional.ofNullable(shapeMap.get(type))
                .orElseThrow(() -> new IllegalArgumentException("Unknown shape type: " + type));

        List<Shape> byType = shapeRepository.findByType(type);
        return byType.stream().map(shapeMapper::mapToDto).collect(Collectors.toList());
    }

    @Transactional
    public ShapeDto changeCircleRadiusToTen(Long id) {
        Shape shape = shapeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Shape not found with ID: " + id));

        if (!"CIRCLE".equalsIgnoreCase(shape.getType())) {
            throw new IllegalArgumentException("Only circles can have their radius changed.");
        }

        shape.setParameters(new double[]{10.0});
        return shapeMapper.mapToDto(shape);
    }


//    @Transactional
//    public ShapeDto changeCircleRadiusToTen(Long id) {
//        Optional<Shape> byId = shapeRepository.findById(id);
//        if (byId.isPresent()) {
//            if (byId.get().getType().equals("CIRCLE")) {
//                byId.get().setParameters(new double[]{10});
//                return shapeMapper.mapToDto(byId.get());
//            }
//        }
//        return null;
//    }


}
