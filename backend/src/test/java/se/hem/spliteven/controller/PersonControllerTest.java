package se.hem.spliteven.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;

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

    @Test
    void delete_withNoDebts_removesPerson() throws Exception {
        Long personId = createTestPerson("Skuldfri", "skuldfri@test.com");

        mockMvc.perform(delete("/api/persons/{id}", personId))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_withDebts_returnConflict() throws Exception {
        Long creatorId = createTestPerson("Skuldberg", "skuldberg@test.com");
        Long accountId = createTestAccount("Skuldkonto1", creatorId);
        Long debtorId = createTestPerson("Gäldenär", "gäldenär@test.com");

        addUserToAccount(accountId, debtorId);
        makeExpense(accountId, debtorId, 1000, LocalDate.now());

        mockMvc.perform(delete("/api/persons/{id}", creatorId))
                .andExpect(status().isConflict());
    }

    @Test
    void createPerson_withDuplicateEmail_returnsConflict() throws Exception {
        createTestPerson("Först", "duplicerad@test.com");

        mockMvc.perform(post("/api/persons")
                .contentType("application/json")
                .content("""
                        {"name":"Sen","email":"duplicerad@test.com","password":"test1234"}
                        """))
                .andExpect(status().isConflict());
    }
}