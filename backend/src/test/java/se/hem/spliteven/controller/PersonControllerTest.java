package se.hem.spliteven.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PersonControllerTest extends AbstractControllerTest {

    @Test
    void createPerson_savesAndReturnsPerson() throws Exception {
        mockMvc.perform(post("/api/persons")
                .contentType("application/json")
                .content("""
                        {"name":"Test Testsson","email":"test@example.com", "password":"testing123"}
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

    @Test
    void getPersons_returnsListOfPersons() throws Exception {
        int before = numberOfPersonsInDatabase();

        createTestPerson("Person", "person@test.com");

        mockMvc.perform(get("/api/persons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(before + 1))
                .andExpect(jsonPath("[?(@.name == 'Person')]").exists());
    }
}