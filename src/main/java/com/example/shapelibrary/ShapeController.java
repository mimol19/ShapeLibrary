package com.example.shapelibrary;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/shapes")
public class ShapeController {
    private ShapeRepository shapeRepository;
    private ShapeFactory shapeFactory;


    @PostMapping
    public ResponseEntity<Shape> addShape(@RequestBody ShapeRequest request) {
        Shape shape = shapeFactory.createShape(request);
        return ResponseEntity.ok(shape);
    }

    @GetMapping
    public ResponseEntity<List<Shape>> getShapesByType(@RequestParam String type) {
        List<Shape> shapes = shapeFactory.getShapesByType(type);
        if (shapes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(shapes);
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }

}
