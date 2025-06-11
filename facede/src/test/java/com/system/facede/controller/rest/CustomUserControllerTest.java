package com.system.facede.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.system.facede.dto.CustomUserBatchUpdateRequest;
import com.system.facede.model.CustomUser;
import com.system.facede.service.CustomUserService;
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
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomUserController.class)
@Import(CustomUserControllerTest.TestConfig.class)
class CustomUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomUserService customUserService;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class TestConfig {
        @Bean public CustomUserService customUserService() {
            return Mockito.mock(CustomUserService.class);
        }
        @Bean public ObjectMapper objectMapper() {
            return new ObjectMapper();
        }
    }

    @Test
    void getAllUsers_shouldReturnEmptyList() throws Exception {
        when(customUserService.getAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/custom-users")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void getUser_shouldReturnUserIfExists() throws Exception {
        CustomUser user = new CustomUser();
        user.setId(1L);
        user.setName("John Doe");

        when(customUserService.getById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/custom-users/1")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void getUser_shouldReturnNotFoundIfMissing() throws Exception {
        when(customUserService.getById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/custom-users/99")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Custom user not found with ID: 99"));
    }

    @Test
    void createUser_shouldReturnSavedUser() throws Exception {
        CustomUser input = new CustomUser();
        input.setName("Jane");
        input.setEmail("jane@example.com");

        CustomUser saved = new CustomUser();
        saved.setId(2L);
        saved.setName("Jane");
        saved.setEmail("jane@example.com");

        when(customUserService.save(any(CustomUser.class))).thenReturn(saved);

        mockMvc.perform(post("/api/custom-users")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.name").value("Jane"));
    }

    @Test
    void updateUser_shouldUpdateAndReturnSuccess() throws Exception {
        CustomUser existing = new CustomUser();
        existing.setId(1L);
        existing.setName("Old");
        existing.setEmail("old@example.com");

        CustomUser updated = new CustomUser();
        updated.setName("New");
        updated.setEmail("new@example.com");
        updated.setPhoneNumber("123456");

        when(customUserService.getById(1L)).thenReturn(Optional.of(existing));
        when(customUserService.save(any(CustomUser.class))).thenReturn(existing);

        mockMvc.perform(put("/api/custom-users/1")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(content().string("User Updated Succesfully"));
    }

    @Test
    void updateUser_shouldReturnErrorIfMissing() throws Exception {
        CustomUser updated = new CustomUser();
        updated.setName("Someone");

        when(customUserService.getById(5L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/custom-users/5")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Custom user not found with ID: 5"));
    }


    @Test
    void updateUserBatch_shouldReturnSuccess() throws Exception {
        CustomUserBatchUpdateRequest requestItem = new CustomUserBatchUpdateRequest();
        requestItem.setUserId(1L);
        requestItem.setEmail("updated@example.com");

        doNothing().when(customUserService).updateUsersBatch(anyList());

        mockMvc.perform(put("/api/custom-users/update-batch")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(requestItem))))
                .andExpect(status().isOk())
                .andExpect(content().string("Batch update successful"));
    }

    @Test
    void deleteCustomUser_shouldReturnSuccessIfExists() throws Exception {
        CustomUser user = new CustomUser();
        user.setId(1L);

        when(customUserService.getById(1L)).thenReturn(Optional.of(user));
        doNothing().when(customUserService).delete(1L);

        mockMvc.perform(delete("/api/custom-users/1")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted user successfully"));
    }

    @Test
    void deleteCustomUser_shouldReturnNotFoundIfMissing() throws Exception {
        when(customUserService.getById(404L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/custom-users/404")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Custom user not found with ID: 404"));
    }
}
