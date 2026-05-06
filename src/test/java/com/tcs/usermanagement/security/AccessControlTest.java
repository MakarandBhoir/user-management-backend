package com.tcs.usermanagement.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AccessControlTest {

    @Autowired
    private MockMvc mockMvc;

    // ── Unauthenticated access should be rejected ──────────────────────────────

    @Test
    void getUsers_unauthenticated_returns401() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getUserById_unauthenticated_returns401() throws Exception {
        mockMvc.perform(get("/users/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createUser_unauthenticated_returns401() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"A\",\"email\":\"a@b.com\",\"password\":\"pass\",\"role\":\"USER\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateUser_unauthenticated_returns401() throws Exception {
        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"A\",\"email\":\"a@b.com\",\"password\":\"pass\",\"role\":\"USER\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteUser_unauthenticated_returns401() throws Exception {
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void sensitiveData_unauthenticated_returns401() throws Exception {
        mockMvc.perform(get("/demo/sensitive-data"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void adminCredentials_unauthenticated_returns401() throws Exception {
        mockMvc.perform(get("/demo/admin/credentials"))
                .andExpect(status().isUnauthorized());
    }

    // ── Only ADMIN can create/update/delete users ──────────────────────────────

    @Test
    void createUser_asAdmin_succeeds() throws Exception {
        mockMvc.perform(post("/users")
                        .with(httpBasic("demo-admin", "demo123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"New User\",\"email\":\"newuser@example.com\",\"password\":\"secret\",\"role\":\"USER\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void sensitiveData_asAdmin_succeeds() throws Exception {
        mockMvc.perform(get("/demo/sensitive-data")
                        .with(httpBasic("demo-admin", "demo123")))
                .andExpect(status().isOk());
    }

    @Test
    void adminCredentials_asAdmin_doesNotExposePassword() throws Exception {
        mockMvc.perform(get("/demo/admin/credentials")
                        .with(httpBasic("demo-admin", "demo123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void adminCredentials_asAdmin_doesNotExposeUsername() throws Exception {
        mockMvc.perform(get("/demo/admin/credentials")
                        .with(httpBasic("demo-admin", "demo123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").doesNotExist());
    }

    // ── Passwords must never appear in API responses ───────────────────────────

    @Test
    void getUsers_passwordNotInResponse() throws Exception {
        mockMvc.perform(get("/users")
                        .with(httpBasic("demo-admin", "demo123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].password").doesNotExist());
    }

    @Test
    void getUserById_passwordNotInResponse() throws Exception {
        mockMvc.perform(get("/users/1")
                        .with(httpBasic("demo-admin", "demo123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void sensitiveData_passwordNotInResponse() throws Exception {
        mockMvc.perform(get("/demo/sensitive-data")
                        .with(httpBasic("demo-admin", "demo123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users[0].password").doesNotExist());
    }
}
