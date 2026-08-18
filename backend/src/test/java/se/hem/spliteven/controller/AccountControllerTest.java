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
class AccountControllerTest extends AbstractControllerTest {

    @Test
    void createAccount_addsCreatorAsFirstMember() throws Exception {
        Long personId = createTestPerson("Test Person", "person@test.com");

        mockMvc.perform(post("/api/accounts")
                .contentType("application/json")
                .content("""
                        {"name":"Testkonto","personId":%d}
                        """.formatted(personId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Testkonto"))
                .andExpect(jsonPath("$.members.length()").value(1))
                .andExpect(jsonPath("$.members[0].id").value(personId));
    }

    @Test
    void addMember_addsSecondPersonToAccount() throws Exception {
        Long creatorId = createTestPerson("Skapare", "skapare@test.com");
        Long newMemberId = createTestPerson("Ny Medlem", "medlem@test.com");

        String accountResponse = mockMvc.perform(post("/api/accounts")
                .contentType("application/json")
                .content("""
                        {"name":"Delat konto","personId":%d}
                        """.formatted(creatorId)))
                .andReturn().getResponse().getContentAsString();
        Long accountId = objectMapper.readTree(accountResponse).get("id").asLong();

        mockMvc.perform(post("/api/accounts/{accountId}/members/{personId}", accountId, newMemberId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.members.length()").value(2));
    }

    @Test
    void removeMember_removesPersonFromAccount() throws Exception {
        Long creatorId = createTestPerson("Kvar", "kvar@test.com");
        Long toRemoveId = createTestPerson("Tas Bort", "borttagen@test.com");

        String accountResponse = mockMvc.perform(post("/api/accounts")
                .contentType("application/json")
                .content("""
                        {"name":"Konto","personId":%d}
                        """.formatted(creatorId)))
                .andReturn().getResponse().getContentAsString();
        Long accountId = objectMapper.readTree(accountResponse).get("id").asLong();

        mockMvc.perform(post("/api/accounts/{accountId}/members/{personId}", accountId, toRemoveId));

        mockMvc.perform(delete("/api/accounts/{accountId}/members/{personId}", accountId, toRemoveId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.members.length()").value(1));
    }

    @Test
    void rename_updatesAccountName() throws Exception {
        Long personId = createTestPerson("Namnbytare", "namn@test.com");

        String accountResponse = mockMvc.perform(post("/api/accounts")
                .contentType("application/json")
                .content("""
                        {"name":"Gammalt namn","personId":%d}
                        """.formatted(personId)))
                .andReturn().getResponse().getContentAsString();
        Long accountId = objectMapper.readTree(accountResponse).get("id").asLong();

        mockMvc.perform(put("/api/accounts/{id}", accountId)
                .contentType("application/json")
                .content("""
                        {"name":"Nytt namn"}
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Nytt namn"));
    }

    @Test
    void getAccountsForPerson_returnsAccountsPersonIsMemberOf() throws Exception {
        Long personId = createTestPerson("Medlem", "medlem@test.com");

        mockMvc.perform(post("/api/accounts")
                .contentType("application/json")
                .content("""
                        {"name":"Mitt konto", "personId":%d}
                        """.formatted(personId)));

        mockMvc.perform(get("/api/persons/{personId}/accounts", personId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("[0].name").value("Mitt konto"));
    }

    @Test
    void getAccountsForPerson_returnsEmptyListWhenNoAccounts() throws Exception {
        Long personId = createTestPerson("Utan konto", "utankonto@test.com");

        mockMvc.perform(get("/api/persons/{personId}/accounts", personId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}