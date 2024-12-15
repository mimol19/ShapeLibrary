package com.example.shapelibrary;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class ShapeControllerTest {

    @Mock
    private ShapeService shapeService;

    @InjectMocks
    private ShapeController shapeController;

    @Test
    void shouldReturnShapesWhenExists() {
        String type = "RECTANGLE";
        List<Shape> shapeList = List.of(new Rectangle(), new Rectangle());
        Mockito.when(shapeService.getShapesByType(type)).thenReturn(shapeList);

        ResponseEntity<List<Shape>> response = shapeController.getShapesByType(type);

        Assertions.assertEquals(2, Objects.requireNonNull(response.getBody()).size());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(shapeList, response.getBody());
    }

    @Test
    void shouldReturnNoContentWhenNoShapesFound() {
        String type = "RECTANGLE";
        Mockito.when(shapeService.getShapesByType(type)).thenReturn(Collections.emptyList());

        ResponseEntity<List<Shape>> response = shapeController.getShapesByType(type);

        Assertions.assertFalse(response.hasBody());
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void shouldCreateShapeSuccessfully(){
        double[] doubles = {15.18};
        ShapeRequest shapeRequest = new ShapeRequest("CIRCLE", doubles);
        Circle circle = new Circle();
        circle.setType("CIRCLE");
        circle.setParameters(doubles);
        Mockito.when(shapeService.createShape(shapeRequest)).thenReturn(circle);

        ResponseEntity<Shape> shapeResponseEntity = shapeController.addShape(shapeRequest);

        Assertions.assertEquals(HttpStatus.OK,shapeResponseEntity.getStatusCode());
        Assertions.assertEquals(circle, shapeResponseEntity.getBody());
    }
}
