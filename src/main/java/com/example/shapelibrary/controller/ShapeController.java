package com.example.shapelibrary.controller;

import com.example.shapelibrary.repository.ShapeRepository;
import com.example.shapelibrary.controller.dto.ShapeDto;
import com.example.shapelibrary.domain.ShapeService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/shapes")
public class ShapeController {
    private ShapeRepository shapeRepository;
    private ShapeService shapeService;

    @PostMapping
    public ResponseEntity<ShapeDto> addShape(@Valid @RequestBody ShapeDto request) {
        ShapeDto shape = shapeService.createShape(request);
        return ResponseEntity.ok(shape);
    }

    @GetMapping
    public ResponseEntity<List<ShapeDto>> getShapesByType(@RequestParam String type) {
        return Optional.of(shapeService.getShapesByType(type))
                .filter(shapes -> !shapes.isEmpty())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @PatchMapping
    public ResponseEntity<ShapeDto> changeCircleRadius(@RequestParam Long id) {
        ShapeDto shape = shapeService.changeCircleRadiusToTen(id);
        return ResponseEntity.ok(shape);
    }
}
