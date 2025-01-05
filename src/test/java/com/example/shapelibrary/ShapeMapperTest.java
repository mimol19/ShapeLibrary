package com.example.shapelibrary;

import com.example.shapelibrary.business.ShapeMapper;
import com.example.shapelibrary.business.ShapeService;
import com.example.shapelibrary.controller.dto.ShapeDto;
import com.example.shapelibrary.repository.entities.Rectangle;
import com.example.shapelibrary.repository.entities.Shape;
import com.example.shapelibrary.repository.entities.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;

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
        rectangle.setParameters(new double[]{4.0, 5.0});

        ShapeDto shapeDto = shapeMapper.mapToDto(rectangle);

        assertEquals(rectangle.getId(), shapeDto.getId());
        assertEquals(rectangle.getType(), shapeDto.getType());
        assertEquals(rectangle.getUser().getName(), shapeDto.getUserName());
    }

    @Test
    void shouldMapShapeDtoToShape() {
        ShapeDto shapeDto = new ShapeDto(1L, "RECTANGLE", new double[]{40.0, 15.0}, "Adam");
        Shape shape = new Rectangle();

        shapeMapper.mapToShape(shapeDto, shape);

        assertNull(shape.getId());
        assertEquals(shape.getType(), shapeDto.getType());
        assertArrayEquals(shape.getParameters(), shapeDto.getParameters());
    }

// TODO
//    @Test
//    void shouldHandleNullUserInShape() {
//
//        Rectangle rectangle = new Rectangle();
//        rectangle.setId(1L);
//        rectangle.setType("RECTANGLE");
//        rectangle.setUser(null);
//        rectangle.setParameters(new double[]{4.0, 5.0});
//
//        ShapeDto shapeDto = shapeMapper.mapToDto(rectangle);
//
//        assertEquals("RECTANGLE", shapeDto.getType());
//        assertNull(shapeDto.getUserName());
//    }
}
