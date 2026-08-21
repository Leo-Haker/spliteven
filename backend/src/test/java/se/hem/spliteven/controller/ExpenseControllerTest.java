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
class ExpenseControllerTest extends AbstractControllerTest {

    @Test
    void createExpense_savesAndReturnsExpense() throws Exception {
        Long personId = createTestPerson("Betalare", "betalare@test.com");
        Long accountId = createTestAccount("Utgiftskonto", personId);
        int amount = 100;

        makeExpense(accountId, personId, amount, LocalDate.now())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(amount))
                .andExpect(jsonPath("$.paidByName").value("Betalare"));
    }

    @Test
    void getByAccount_returnsExpensesForThatAccount() throws Exception {
        Long personId = createTestPerson("Person", "person2@test.com");
        Long accountId = createTestAccount("Konto med utgifter", personId);

        mockMvc.perform(post("/api/expense")
                .contentType("application/json")
                .content("""
                        {"accountId":%d,"paidById":%d,"income":false,
                         "description":"Hyra","amount":5000.00,"date":"2026-07-01"}
                        """.formatted(accountId, personId)));

        mockMvc.perform(get("/api/expense/account/{accountId}", accountId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].description").value("Hyra"));
    }
}