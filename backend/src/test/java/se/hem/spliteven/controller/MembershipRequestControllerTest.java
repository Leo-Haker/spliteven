package se.hem.spliteven.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import se.hem.spliteven.service.EmailService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MembershipRequestControllerTest extends AbstractControllerTest {

    // Replaces real emailService with an "empty" mock during tests
    @MockitoBean
    private EmailService emailService;

    @Test
    void create_savesRequestForPersonFoundByEmail() throws Exception {
        Long creatorId = createTestPerson("Skapare", "skapare@test.com");
        Long accountId = createTestAccount("konto", creatorId);
        createTestPerson("Inbjuden", "inbjuden@test.com");

        mockMvc.perform(post("/api/accounts/{accountId}/requests", accountId)
                .contentType("application/json")
                .content("""
                        {"email":"inbjuden@test.com"}
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value(accountId))
                .andExpect(jsonPath("$.accountName").value("konto"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void create_withUnknownEmail_returnsError() throws Exception {
        Long creatorId = createTestPerson("Skapare", "skapare2@test.com");
        Long accountId = createTestAccount("Konto", creatorId);

        mockMvc.perform(post("/api/accounts/{accountId}/requests", accountId)
                .contentType("application/json")
                .content("""
                        {"email":"finns-inte@test.com"}
                        """))
                .andExpect(status().isNotFound());
    }

    @Test
    void getPending_returnsOnlyPendingRequestsForThatPerson() throws Exception {
        Long creatorId = createTestPerson("Skapare", "skapare3@test.com");
        Long accountId = createTestAccount("Konto3", creatorId);
        Long invitedId = createTestPerson("Inbjuden3", "inbjuden3@test.com");

        createTestRequest(accountId, "inbjuden3@test.com");

        mockMvc.perform(get("/api/requests/{personId}/requests", invitedId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].accountName").value("Konto3"))
                .andExpect(jsonPath("$[0].status").value("PENDING"));
    }

    @Test
    void getPending_returnsEmptyListWhenNoRequests() throws Exception {
        Long personId = createTestPerson("Ensam", "ensam@test.com");

        mockMvc.perform(get("/api/requests/{personId}/requests", personId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void accept_addsPersonToAccountAndUpdatesStatus() throws Exception {
        Long creatorId = createTestPerson("Skapare4", "skapare4@test.com");
        Long accountId = createTestAccount("Konto4", creatorId);
        Long invitedId = createTestPerson("Inbjuden4", "inbjuden4@test.com");

        Long requestId = createTestRequest(accountId, "inbjuden4@test.com");

        mockMvc.perform(post("/api/requests/{id}/accept", requestId))
                .andExpect(status().isOk());

        // Bekräfta att personen nu faktiskt är medlem i kontot
        mockMvc.perform(get("/api/accounts/{id}", accountId))
                .andExpect(jsonPath("$.members.length()").value(2));

        // Bekräfta att förfrågan inte längre är pending
        mockMvc.perform(get("/api/requests/{personId}/requests", invitedId))
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void decline_updatesStatusButDoesNotAddPersonToAccount() throws Exception {
        Long creatorId = createTestPerson("Skapare5", "skapare5@test.com");
        Long accountId = createTestAccount("Konto5", creatorId);
        Long invitedId = createTestPerson("Inbjuden5", "inbjuden5@test.com");

        Long requestId = createTestRequest(accountId, "inbjuden5@test.com");

        mockMvc.perform(post("/api/requests/{id}/decline", requestId))
                .andExpect(status().isOk());

        // Personen ska INTE ha lagts till i kontot
        mockMvc.perform(get("/api/accounts/{id}", accountId))
                .andExpect(jsonPath("$.members.length()").value(1));

        // Förfrågan är inte längre pending
        mockMvc.perform(get("/api/requests/{personId}/requests", invitedId))
                .andExpect(jsonPath("$.length()").value(0));
    }

}
