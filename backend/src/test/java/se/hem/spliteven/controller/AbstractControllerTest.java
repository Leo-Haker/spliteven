package se.hem.spliteven.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

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
                        {"name":"%s","email":"%s"}
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
}