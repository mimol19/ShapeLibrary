package com.example.shapelibrary;

import com.example.shapelibrary.business.ShapeMapper;
import com.example.shapelibrary.business.ShapeService;
import com.example.shapelibrary.business.UserService;
import com.example.shapelibrary.controller.dto.ShapeDto;
import com.example.shapelibrary.repository.UserRepository;
import com.example.shapelibrary.repository.ShapeRepository;
import com.example.shapelibrary.repository.entities.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShapeServiceTest {

    @Mock
    private ShapeRepository shapeRepository;

    @Mock
    private UserService userService;
    @Mock
    private ShapeMapper shapeMapper;

    private ShapeService shapeService;

    @BeforeEach
    void setup() {
        Map<String, Shape> shapeMap = new HashMap<>();
        shapeMap.put("CIRCLE", new Circle());
        shapeMap.put("SQUARE", new Square());

        shapeService = new ShapeService(new ArrayList<>(shapeMap.values()), shapeRepository, userService, shapeMapper);
    }

    @Test
    void shouldCreateShapeSuccessfully() {
        String type = "CIRCLE";
        ShapeDto shapeDto = ShapeDto.builder()
                .type(type)
                .parameters(new double[]{10})
                .userName("John")
                .build();
        User user = User.builder().id(1L).name("John").build();

        Shape shape = shapeService.shapeMap.get(type);

        when(userService.getOrCreateUser("John")).thenReturn(user);
        when(shapeMapper.mapToDto(any(Shape.class))).thenReturn(shapeDto);
        when(shapeRepository.save(any(Shape.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ShapeDto result = shapeService.createShape(shapeDto);

        assertNotNull(result);
        assertEquals(type, result.getType());
        assertEquals("John", result.getUserName());
        verify(userService, times(1)).getOrCreateUser("John");
        verify(shapeRepository, times(1)).save(any(Shape.class));
    }

    @Test
    void shouldThrowExceptionForUnknownShapeType() {
        double[] doubles = {15.18, 12.3};
        ShapeDto request = new ShapeDto(4L, "TRIANGLE", doubles, "John");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> shapeService.createShape(request)
        );

        assertEquals("Unknown shape type: TRIANGLE", exception.getMessage());
    }

    @Test
    void shouldGetShapesByTypeSuccessfully() {
        String type = "CIRCLE";
        Shape circle1 = new Circle();
        circle1.setId(1L);
        circle1.setType(type);
        circle1.setParameters(new double[]{10});
        circle1.setUser(User.builder().name("Marry").build());

        Shape circle2 = new Circle();
        circle2.setId(2L);
        circle2.setType(type);
        circle2.setParameters(new double[]{15});
        circle2.setUser(User.builder().name("Jane").build());

        ShapeDto expectedDto1 = ShapeDto.builder()
                .id(1L)
                .type(type)
                .userName("Marry")
                .parameters(new double[]{10})
                .build();

        ShapeDto expectedDto2 = ShapeDto.builder()
                .id(2L)
                .type(type)
                .userName("Jane")
                .parameters(new double[]{15})
                .build();

        when(shapeRepository.findByType(type)).thenReturn(List.of(circle1, circle2));
        when(shapeMapper.mapToDto(circle1)).thenReturn(expectedDto1);
        when(shapeMapper.mapToDto(circle2)).thenReturn(expectedDto2);

        List<ShapeDto> result = shapeService.getShapesByType(type);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(circle1.getUser().getName(), result.getFirst().getUserName());
        assertEquals(circle2.getUser().getName(), result.get(1).getUserName());
        verify(shapeRepository, times(1)).findByType(type);
    }

    @Test
    void shouldThrowExceptionIfUnknownShape() {
        String type = "HEXAGON";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> shapeService.getShapesByType(type)
        );

        assertEquals("Unknown shape type: " + type, exception.getMessage());
        verify(shapeRepository, times(0)).findByType(type);
    }

    @Test
    void shouldThrowExceptionIfNoShapesOfType() {
        // Arrange
        when(shapeRepository.findByType("CIRCLE")).thenReturn(List.of());

        // Act
        List<ShapeDto> result = shapeService.getShapesByType("CIRCLE");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(shapeRepository).findByType("CIRCLE");
    }


    @Test
    void shouldChangeCircleRadiusToTenWhenShapeExists() {
        User user = new User(1L, "John Doe", Collections.emptyList());
        Long shapeId = 1L;
        Shape circle = new Circle();
        circle.setId(shapeId);
        circle.setType("CIRCLE");
        circle.setParameters(new double[]{5});
        circle.setUser(user);

        ShapeDto expectedDto = ShapeDto.builder()
                .id(shapeId)
                .type("CIRCLE")
                .userName("John Doe")
                .parameters(new double[]{10})
                .build();

        when(shapeRepository.findById(shapeId)).thenReturn(Optional.of(circle));
        when(shapeMapper.mapToDto(circle)).thenReturn(expectedDto);

        ShapeDto result = shapeService.changeCircleRadiusToTen(shapeId);

        assertNotNull(result);
        assertEquals(10.0, result.getParameters()[0]);
        verify(shapeRepository, times(1)).findById(shapeId);
    }

    @Test
    void shouldThrowExceptionWhenShapeIsNotCircle() {
        Long shapeId = 1L;
        Shape square = new Square();
        square.setId(shapeId);
        square.setType("SQUARE");
        when(shapeRepository.findById(shapeId)).thenReturn(Optional.of(square));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> shapeService.changeCircleRadiusToTen(shapeId)
        );

        assertEquals("Only circles can have their radius changed.", exception.getMessage());
        verify(shapeRepository, times(1)).findById(shapeId);
    }

    @Test
    void shouldThrowExceptionWhenShapeNotFound() {
        Long shapeId = 1L;
        when(shapeRepository.findById(shapeId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> shapeService.changeCircleRadiusToTen(shapeId)
        );

        assertEquals("Shape not found with ID: " + shapeId, exception.getMessage());
        verify(shapeRepository, times(1)).findById(shapeId);
    }
}
