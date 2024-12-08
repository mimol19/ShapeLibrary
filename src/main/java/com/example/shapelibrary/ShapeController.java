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
    private ShapeService shapeService;

    @PostMapping
    public ResponseEntity<ShapeEntity> addShape(@RequestBody ShapeRequest request) {
        ShapeEntity entity = shapeService.addShape(request.getType(), request.getParameters());
        return ResponseEntity.ok(entity);
    }

    @GetMapping
    public ResponseEntity<List<ShapeEntity>> getShapesByType(@RequestParam String type) {
        List<ShapeEntity> shapes = shapeService.getShapesByType(type);
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
