package com.system.facede.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.system.facede.model.AdminUser;
import com.system.facede.service.AdminUserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminUserController.class)
@Import(AdminUserControllerTest.TestConfig.class)
class AdminUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class TestConfig {
        @Bean public AdminUserService adminUserService() { return Mockito.mock(AdminUserService.class); }
        @Bean public ObjectMapper objectMapper() { return new ObjectMapper(); }
    }

    @Test
    void getAllAdmins_shouldReturnEmptyList() throws Exception {
        when(adminUserService.getAllAdmins()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/admin-users")
                        .with(user("superadmin").roles("SUPER_ADMIN")))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void getAdmin_shouldReturnAdminIfExists() throws Exception {
        AdminUser user = new AdminUser();
        user.setId(1L);
        user.setUsername("superadmin");
        user.setPassword("secure");

        when(adminUserService.getById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/admin-users/1")
                        .with(user("superadmin").roles("SUPER_ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("superadmin"));
    }

    @Test
    void getAdmin_shouldReturnNotFoundIfMissing() throws Exception {
        when(adminUserService.getById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/admin-users/1")
                        .with(user("superadmin").roles("SUPER_ADMIN")))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Admin user not found with ID: 1"));
    }

    @Test
    void createAdmin_shouldReturnCreatedUser() throws Exception {
        AdminUser input = new AdminUser();
        input.setUsername("admin1");
        input.setPassword("secret123");

        AdminUser saved = new AdminUser();
        saved.setId(10L);
        saved.setUsername("admin1");
        saved.setPassword("secret123");

        when(adminUserService.save(any(AdminUser.class))).thenReturn(saved);

        mockMvc.perform(post("/api/admin-users")
                        .with(user("superadmin").roles("SUPER_ADMIN"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.username").value("admin1"));
    }

    @Test
    void deleteAdmin_shouldReturnSuccessIfExists() throws Exception {
        AdminUser existing = new AdminUser();
        existing.setId(1L);
        existing.setUsername("admin2");

        when(adminUserService.getById(1L)).thenReturn(Optional.of(existing));
        doNothing().when(adminUserService).delete(1L);

        mockMvc.perform(delete("/api/admin-users/1")
                        .with(user("superadmin").roles("SUPER_ADMIN"))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted admin successfully"));
    }

    @Test
    void deleteAdmin_shouldReturnNotFoundIfMissing() throws Exception {
        when(adminUserService.getById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/admin-users/99")
                        .with(user("superadmin").roles("SUPER_ADMIN"))
                        .with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Admin user not found with ID: 99"));
    }
}
