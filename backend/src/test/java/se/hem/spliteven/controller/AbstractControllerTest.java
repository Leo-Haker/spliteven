package se.hem.spliteven.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public abstract class AbstractControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    protected final ObjectMapper objectMapper = new ObjectMapper();

    protected Long createTestPerson(String name, String email) throws Exception {
        String response = mockMvc.perform(post("/api/persons")
                .contentType("application/json")
                .content("""
                        {"name":"%s","email":"%s", "password":"test1234"}
                        """.formatted(name, email)))
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readTree(response).get("id").asLong();
    }

    protected Long createTestAccount(String name, Long personId) throws Exception {
        String response = mockMvc.perform(post("/api/accounts")
                .contentType("application/json")
                .content("""
                        {"name":"%s","personId":%d}
                        """.formatted(name, personId)))
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readTree(response).get("id").asLong();
    }

    protected Long createTestRequest(Long accountId, String email) throws Exception {
        String response = mockMvc.perform(post("/api/accounts/{accountId}/requests", accountId)
                .contentType("application/json")
                .content("""
                        {"email":"%s"}
                        """.formatted(email)))
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readTree(response).get("id").asLong();
    }

    protected int numberOfPersonsInDatabase() throws Exception {
        String before = mockMvc.perform(get("/api/persons"))
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(before).size();

    }

    protected ResultActions addUserToAccount(Long accountId, Long userId) throws Exception {
        return mockMvc.perform(post("/api/accounts/{accountId}/members/{personId}", accountId, userId));
    }

    protected ResultActions makeExpense(Long accountId, Long userId, int amount, LocalDate date) throws Exception {
        return mockMvc.perform(post("/api/expense")
                .contentType("application/json")
                .content("""
                        {"accountId":%d, "paidById":%d, "income":false,
                         "description":"info","amount":%d,"date":"%s"
                        }
                        """.formatted(accountId, userId, amount, date)));
    }

    protected ResultActions makeIncome(Long accountId, Long userId, int amount, LocalDate date) throws Exception {
        return mockMvc.perform(post("/api/expense")
                .contentType("application/json")
                .content("""
                        {"accountId":%d, "paidById":%d, "income":true,
                         "description":"info","amount":%d,"date":"%s"
                        }
                        """.formatted(accountId, userId, amount, date)));
    }
}