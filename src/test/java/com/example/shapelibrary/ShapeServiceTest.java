package com.example.shapelibrary;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class ShapeServiceTest {

    @Mock
    private ShapeRepository shapeRepository;

    @Mock
    private Circle circle; // przykład dla kształtu Circle

    @Mock
    private Square square; // przykład dla kształtu Square

    private ShapeService shapeService;

    @BeforeEach
    void setUp() {
        List<Shape> shapes = List.of(circle, square);
        Mockito.when(circle.getType()).thenReturn("CIRCLE");
        Mockito.when(square.getType()).thenReturn("SQUARE");
        shapeService = new ShapeService(shapes, shapeRepository);
    }

    @Test
    void shouldCreateShapeSuccessfully() {
        // Arrange
        double[] doubles = {15.18};
        ShapeRequest request = new ShapeRequest("CIRCLE", doubles);
        Mockito.doNothing().when(circle).setParameters(request.getParameters());
        Mockito.doNothing().when(circle).setType(request.getType());
        Mockito.doNothing().when(circle).setId(null);
        Mockito.when(shapeRepository.save(circle)).thenReturn(circle);

        // Act
        Shape result = shapeService.createShape(request);

        // Assert
        assertNotNull(result);
        Mockito.verify(circle).setParameters(request.getParameters());
        Mockito.verify(shapeRepository).save(circle);
    }

    @Test
    void shouldThrowExceptionForUnknownShapeType() {
        // Arrange
        double[] doubles = {15.18, 12.3};
        ShapeRequest request = new ShapeRequest("TRIANGLE", doubles);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> shapeService.createShape(request)
        );
        assertEquals("Unknown shape type: TRIANGLE", exception.getMessage());
    }

    @Test
    void shouldGetShapesByTypeSuccessfully() {
        // Arrange
        List<Shape> shapes = List.of(circle);
        Mockito.when(shapeRepository.findByType("CIRCLE")).thenReturn(shapes);

        // Act
        List<Shape> result = shapeService.getShapesByType("CIRCLE");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(circle, result.getFirst());
        Mockito.verify(shapeRepository).findByType("CIRCLE");
    }

    @Test
    void shouldReturnEmptyListIfNoShapesOfType() {
        // Arrange
        Mockito.when(shapeRepository.findByType("HEXAGON")).thenReturn(List.of());

        // Act
        List<Shape> result = shapeService.getShapesByType("HEXAGON");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        Mockito.verify(shapeRepository).findByType("HEXAGON");
    }
}
