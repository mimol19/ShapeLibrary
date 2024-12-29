package com.example.shapelibrary.controller;

import com.example.shapelibrary.repository.ShapeRepository;
import com.example.shapelibrary.controller.dto.ShapeDto;
import com.example.shapelibrary.business.ShapeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/shapes")
public class ShapeController {
    private ShapeRepository shapeRepository;
    private ShapeService shapeService;


    @PostMapping
    public ResponseEntity<ShapeDto> addShape(@RequestBody ShapeDto request) {
        ShapeDto shape = shapeService.createShape(request);
        return ResponseEntity.ok(shape);
    }

    @GetMapping
    public ResponseEntity<List<ShapeDto>> getShapesByType(@RequestParam String type) {
        List<ShapeDto> shapes = shapeService.getShapesByType(type);
        if (shapes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(shapes);
    }

    @PatchMapping
    public ResponseEntity<ShapeDto> changeCircleRadius(@RequestParam Long id) {
        ShapeDto shape = shapeService.changeCircleRadiusToTen(id);
        return ResponseEntity.ok(shape);
    }
}
