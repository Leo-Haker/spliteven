package se.hem.spliteven.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createPerson_savesAndReturnsPerson() throws Exception {
        mockMvc.perform(post("/api/persons")
                .contentType("application/json")
                .content("""
                        {"name":"Test Testsson","email":"test@example.com"}
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Testsson"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void createPerson_withInvalidEmail_returnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/persons")
                .contentType("application/json")
                .content("""
                        {"name":"Test","email":"inte-en-email"}
                        """))
                .andExpect(status().isBadRequest());
    }
}