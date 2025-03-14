package com.example.shapelibrary;

import com.example.shapelibrary.controller.dto.ShapeDto;
import com.example.shapelibrary.repository.ShapeRepository;
import com.example.shapelibrary.repository.UserRepository;
import com.example.shapelibrary.repository.entities.Circle;
import com.example.shapelibrary.repository.entities.Shape;
import com.example.shapelibrary.repository.entities.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:testdb")
@AutoConfigureMockMvc
public class ShapeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ShapeRepository shapeRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        shapeRepository.deleteAll();
        userRepository.deleteAll();
    }

    private User createUser(String name) {
        return userRepository.save(User.builder().name(name).build());
    }

    private Shape createCircle(double parameter, User user) {
        Shape circle = new Circle();
        circle.setType("CIRCLE");
        circle.setParameters(List.of(parameter));
        circle.setUser(user);
        return shapeRepository.save(circle);
    }

    @Test
    void shouldAddShapeSuccessfully() throws Exception {
        String json1 = """
                {
                "type": "CIRCLE",
                "parameters": [15.0],
                "userName": "John"
                }
                """;

        ResultActions result = mockMvc.perform(post("/api/v1/shapes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("CIRCLE"))
                .andExpect(jsonPath("$.userName").value("John"))
                .andExpect(jsonPath("$.parameters[0]").value(15.0));

        ShapeDto createdShape1 = objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ShapeDto.class);
        assertNotNull(createdShape1.getId());

        Shape shapeInDb1 = shapeRepository.findById(createdShape1.getId()).orElseThrow();
        assertEquals("CIRCLE", shapeInDb1.getType());
        assertEquals("John", shapeInDb1.getUser().getName());
        assertEquals(List.of(15.0), shapeInDb1.getParameters());
    }

    @Test
    void shouldReturnBadRequestWhenParametersAreInvalid() throws Exception {
        String invalidJson = """
                {
                "type": "CIRCLE",
                "parameters": [],
                "userName": "John"
                }
                """;

        mockMvc.perform(post("/api/v1/shapes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetShapesSuccessfully() throws Exception {
        User user1 = createUser("John");
        User user2 = createUser("Jane");

        createCircle(10.0, user1);
        createCircle(15.0, user2);

        mockMvc.perform(get("/api/v1/shapes?type=CIRCLE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].type").value("CIRCLE"))
                .andExpect(jsonPath("$[0].userName").value("John"))
                .andExpect(jsonPath("$[0].parameters[0]").value(10.0))
                .andExpect(jsonPath("$[1].type").value("CIRCLE"))
                .andExpect(jsonPath("$[1].userName").value("Jane"))
                .andExpect(jsonPath("$[1].parameters[0]").value(15.0));

        List<Shape> shapesInDb = shapeRepository.findByType("CIRCLE");
        assertEquals(2, shapesInDb.size());

        assertEquals("John", shapesInDb.get(0).getUser().getName());
        assertEquals(10.0, shapesInDb.get(0).getParameters().get(0), 0.01);
        assertEquals("Jane", shapesInDb.get(1).getUser().getName());
        assertEquals(15.0, shapesInDb.get(1).getParameters().get(0), 0.01);
    }

    @Test
    void shouldPatchShapesSuccessfully() throws Exception {
        User user = createUser("John");
        Shape shape = createCircle(15.0, user);
        Long shapeId = shape.getId();

        ResultActions result = mockMvc.perform(patch("/api/v1/shapes?id=" + shapeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.parameters[0]").value(10.0))
                .andExpect(jsonPath("$.type").value("CIRCLE"))
                .andExpect(jsonPath("$.userName").value("John"));

        ShapeDto createdShape1 = objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ShapeDto.class);
        assertNotNull(createdShape1.getId());

        Shape shapeInDb1 = shapeRepository.findById(createdShape1.getId()).orElseThrow();
        assertEquals("CIRCLE", shapeInDb1.getType());
        assertEquals("John", shapeInDb1.getUser().getName());
        assertEquals(List.of(10.0), shapeInDb1.getParameters());
    }

    @Test
    void shouldGetUsersSuccessfully() throws Exception {

        createCircle(10.0, createUser("John"));
        createCircle(10.0, createUser("Jane"));

        ResultActions result = mockMvc.perform(get("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("John"))
                .andExpect(jsonPath("$[1].name").value("Jane"));

        List<User> usersInDb = userRepository.findAll();
        assertEquals(2, usersInDb.size());


        assertEquals("John", usersInDb.get(0).getName());
        assertEquals("Jane", usersInDb.get(1).getName());
    }

    @Test
    void shouldReturnNotFoundForNonExistingShape() throws Exception {
        mockMvc.perform(get("/api/v1/shapes?type=RECTANGLE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
