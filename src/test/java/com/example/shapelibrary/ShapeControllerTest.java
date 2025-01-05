package com.example.shapelibrary;

import com.example.shapelibrary.repository.ShapeRepository;
import com.example.shapelibrary.repository.UserRepository;
import com.example.shapelibrary.repository.entities.Circle;
import com.example.shapelibrary.repository.entities.Shape;
import com.example.shapelibrary.repository.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:testdb")
@AutoConfigureMockMvc
public class ShapeControllerTest {

    @Autowired
    private MockMvc mockMvc;

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
        circle.setParameters(new double[]{parameter});
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

        mockMvc.perform(post("/api/v1/shapes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("CIRCLE"))
                .andExpect(jsonPath("$.userName").value("John"));
    }

    @Test
    void shouldGetShapesSuccessfully() throws Exception {
        User user1 = createUser("John");
        User user2 = createUser("Jane");

        createCircle(10.0, user1);
        createCircle(10.0, user2);

        mockMvc.perform(get("/api/v1/shapes?type=CIRCLE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].type").value("CIRCLE"))
                .andExpect(jsonPath("$[0].userName").value("John"))
                .andExpect(jsonPath("$[0].parameters[0]").value(10.0))
                .andExpect(jsonPath("$[1].type").value("CIRCLE"))
                .andExpect(jsonPath("$[1].userName").value("Jane"));
    }

    @Test
    void shouldPatchShapesSuccessfully() throws Exception {
        User user = createUser("John");
        Shape shape = createCircle(15.0, user);
        Long shapeId = shape.getId();

        mockMvc.perform(patch("/api/v1/shapes?id=" + shapeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.parameters[0]").value(10.0))
                .andExpect(jsonPath("$.type").value("CIRCLE"))
                .andExpect(jsonPath("$.userName").value("John"));
    }

    @Test
    void shouldGetUsersSuccessfully() throws Exception {
//
//        Shape createdShape1 = objectMapper.readValue(result1.getResponse().getContentAsString(), Shape.class);
//        assertNotNull(createdShape1.getId());
//
//        // Weryfikacja w bazie
//        Shape shapeInDb1 = shapeRepository.findById(createdShape1.getId()).orElseThrow();
//        assertEquals("CIRCLE", shapeInDb1.getType());
//        assertEquals("John", shapeInDb1.getCreatorName());
        // TODO

        createCircle(10.0, createUser("John"));
        createCircle(10.0, createUser("Jane"));

        mockMvc.perform(get("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("John"))
                .andExpect(jsonPath("$[1].name").value("Jane"));
    }

    @Test
    void shouldReturnNotFoundForNonExistingShape() throws Exception {
        mockMvc.perform(get("/api/v1/shapes?type=RECTANGLE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
