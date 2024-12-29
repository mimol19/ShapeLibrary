package com.example.shapelibrary;

import com.example.shapelibrary.business.ShapeService;
import com.example.shapelibrary.controller.dto.ShapeDto;
import com.example.shapelibrary.repository.CreatorRepository;
import com.example.shapelibrary.repository.ShapeRepository;
import com.example.shapelibrary.repository.entities.*;
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
    private CreatorRepository creatorRepository;

    private ShapeService shapeService;

    @BeforeEach
    void setup() {
        Map<String, Shape> shapeMap = new HashMap<>();
        shapeMap.put("CIRCLE", new Circle());
        shapeMap.put("SQUARE", new Square());

        shapeService = new ShapeService(new ArrayList<>(shapeMap.values()), shapeRepository, creatorRepository);
    }

    @Test
    void shouldCreateShapeSuccessfully() {
        String type = "CIRCLE";
        ShapeDto shapeDto = ShapeDto.builder()
                .type(type)
                .parameters(new double[]{10})
                .creatorName("John")
                .build();
        Creator creator = Creator.builder().id(1L).name("John").build();

        when(creatorRepository.findByName("John")).thenReturn(Optional.of(creator));
        when(shapeRepository.save(any(Shape.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ShapeDto result = shapeService.createShape(shapeDto);

        assertNotNull(result);
        assertEquals(type, result.getType());
        assertEquals("John", result.getCreatorName());
        verify(creatorRepository, times(1)).findByName("John");
        verify(shapeRepository, times(1)).save(any(Shape.class));
    }

    @Test
    void shouldThrowExceptionForUnknownShapeType() {
        // Arrange
        double[] doubles = {15.18, 12.3};
        ShapeDto request = new ShapeDto(4L, "TRIANGLE", doubles, "John");

        // Act & Assert
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
        circle1.setCreator(Creator.builder().name("Marry").build());

        Shape circle2 = new Circle();
        circle2.setId(2L);
        circle2.setType(type);
        circle2.setParameters(new double[]{15});
        circle2.setCreator(Creator.builder().name("Jane").build());

        when(shapeRepository.findByType(type)).thenReturn(List.of(circle1, circle2));

        List<ShapeDto> result = shapeService.getShapesByType(type);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(circle1.getCreator().getName(), result.getFirst().getCreatorName());
        assertEquals(circle2.getCreator().getName(), result.get(1).getCreatorName());
        verify(shapeRepository, times(1)).findByType(type);
    }

    @Test
    void shouldReturnEmptyListIfNoShapesOfType() {
        // Arrange
        when(shapeRepository.findByType("HEXAGON")).thenReturn(List.of());

        // Act
        List<ShapeDto> result = shapeService.getShapesByType("HEXAGON");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(shapeRepository).findByType("HEXAGON");
    }


    @Test
    void shouldChangeCircleRadiusToTenWhenShapeExists() {
        Creator creator = new Creator(1L, "John Doe", Collections.emptyList());
        Long shapeId = 1L;
        Shape circle = new Circle();
        circle.setId(shapeId);
        circle.setType("CIRCLE");
        circle.setParameters(new double[]{5});
        circle.setCreator(creator);
        when(shapeRepository.findById(shapeId)).thenReturn(Optional.of(circle));

        ShapeDto result = shapeService.changeCircleRadiusToTen(shapeId);

        assertNotNull(result);
        assertEquals(10.0, result.getParameters()[0]);
        verify(shapeRepository, times(1)).findById(shapeId);
    }

    @Test
    void shouldReturnNullWhenShapeNotFound() {
        Long shapeId = 1L;
        when(shapeRepository.findById(shapeId)).thenReturn(Optional.empty());

        ShapeDto result = shapeService.changeCircleRadiusToTen(shapeId);

        assertNull(result);
        verify(shapeRepository, times(1)).findById(shapeId);
    }

    @Test
    void shouldReturnNullWhenShapeIsNotCircle() {
        Long shapeId = 1L;
        Shape square = new Square();
        square.setId(shapeId);
        square.setType("SQUARE");
        when(shapeRepository.findById(shapeId)).thenReturn(Optional.of(square));

        ShapeDto result = shapeService.changeCircleRadiusToTen(shapeId);

        assertNull(result);
        verify(shapeRepository, times(1)).findById(shapeId);
    }


    @Test
    void shouldReturnExistingCreator() {
        String creatorName = "John";
        Creator existingCreator = Creator.builder().name(creatorName).build();
        when(creatorRepository.findByName(creatorName)).thenReturn(Optional.of(existingCreator));

        Creator result = shapeService.getOrCreateCreator(creatorName);

        assertEquals(existingCreator, result);
        verify(creatorRepository, times(1)).findByName(creatorName);
        verify(creatorRepository, times(0)).save(any(Creator.class));
    }

    @Test
    void shouldCreateNewCreatorWhenNotExist() {
        String creatorName = "Jane";

        when(creatorRepository.findByName(creatorName)).thenReturn(Optional.empty());
        Creator creator = Creator.builder().name(creatorName).build();
        when(creatorRepository.save(any(Creator.class))).thenReturn(creator);

        Creator result = shapeService.getOrCreateCreator(creatorName);

        assertNotNull(result);
        assertEquals(creatorName, result.getName());
        verify(creatorRepository, times(1)).findByName(creatorName);
        verify(creatorRepository, times(1)).save(any(Creator.class));
    }


    @Test
    void shouldMapShapeToShapeDto() {
        Creator creator = new Creator(1L, "John Doe", Collections.emptyList());
        Rectangle rectangle = new Rectangle();
        rectangle.setId(1L);
        rectangle.setType("RECTANGLE");
        rectangle.setCreator(creator);
        rectangle.setParameters(new double[]{4.0, 5.0});

        ShapeDto shapeDto = ShapeService.mapToDto(rectangle);

        assertEquals(rectangle.getId(), shapeDto.getId());
        assertEquals(rectangle.getType(), shapeDto.getType());
        assertEquals(rectangle.getCreator().getName(), shapeDto.getCreatorName());
    }

    @Test
    void shouldMapShapeDtoToShape() {
        ShapeDto shapeDto = new ShapeDto(1L, "RECTANGLE", new double[]{40.0, 15.0}, "Adam");
        Shape shape = new Rectangle();

        ShapeService.mapToShape(shapeDto, shape);

        assertNull(shape.getId());
        assertEquals(shape.getType(), shapeDto.getType());
        assertArrayEquals(shape.getParameters(), shapeDto.getParameters());
    }


}
