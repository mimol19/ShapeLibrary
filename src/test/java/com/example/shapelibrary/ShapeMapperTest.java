package com.example.shapelibrary;

import com.example.shapelibrary.domain.ShapeMapper;
import com.example.shapelibrary.controller.dto.ShapeDto;
import com.example.shapelibrary.repository.entities.Rectangle;
import com.example.shapelibrary.repository.entities.Shape;
import com.example.shapelibrary.repository.entities.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ShapeMapperTest {

    private ShapeMapper shapeMapper = new ShapeMapper();

    @Test
    void shouldMapShapeToShapeDto() {
        User user = new User(1L, "John Doe", Collections.emptyList());
        Rectangle rectangle = new Rectangle();
        rectangle.setId(1L);
        rectangle.setType("RECTANGLE");
        rectangle.setUser(user);
        rectangle.setParameters(List.of(4.0, 5.0));

        ShapeDto shapeDto = shapeMapper.mapToDto(rectangle);

        assertEquals(rectangle.getId(), shapeDto.getId());
        assertEquals(rectangle.getType(), shapeDto.getType());
        assertEquals(rectangle.getUser().getName(), shapeDto.getUserName());
    }

    @Test
    void shouldMapShapeDtoToShape() {
        ShapeDto shapeDto = new ShapeDto(1L, "RECTANGLE", List.of(40.0, 15.0), "Adam");
        Shape shape = new Rectangle();

        shapeMapper.mapToShape(shapeDto, shape);

        assertNull(shape.getId());
        assertEquals(shape.getType(), shapeDto.getType());
        assertEquals(shape.getParameters(), shapeDto.getParameters());
    }
}
