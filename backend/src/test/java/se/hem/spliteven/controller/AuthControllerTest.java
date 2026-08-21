package se.hem.spliteven.controller;

import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AuthControllerTest extends AbstractControllerTest {

    @Test
    void login_withCorrectCredentials_returnsPerson() throws Exception {
        String email = "inloggare@test.com";
        String password = "secret123";

        mockMvc.perform(post("/api/persons")
                .contentType("application/json")
                .content("""
                        {"name":"Inloggare","email":"%s","password":"%s"}
                        """.formatted(email, password)));

        mockMvc.perform(post("/api/auth/login")
                .contentType("application/json")
                .content("""
                        {"email":"%s","password":"%s"}
                        """.formatted(email, password)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.name").value("Inloggare"))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void login_withWrongPassword_returnsError() throws Exception {
        mockMvc.perform(post("/api/persons")
                .contentType("application/json")
                .content("""
                        {"name":"Person2","email":"person2@test.com","password":"ratttlosenord"}
                        """));

        mockMvc.perform(post("/api/auth/login")
                .contentType("application/json")
                .content("""
                        {"email":"person2@test.com","password":"feltlosenord"}
                        """))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void login_withUnknownEmail_returnsError() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                .contentType("application/json")
                .content("""
                        {"email":"finnsinte@test.com","password":"nagotlosenord"}
                        """))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void createPerson_passwordIsNeverExposedInResponse() throws Exception {
        mockMvc.perform(post("/api/persons")
                .contentType("application/json")
                .content("""
                        {"name":"Säker","email":"saker@test.com","password":"hemligt123"}
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.passwordHash").doesNotExist());
    }

}
