package com.system.facede.controller.view;

import com.system.facede.model.CustomUser;
import com.system.facede.service.CustomUserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomUserViewController.class)
@Import(CustomUserViewControllerTest.TestConfig.class)
class CustomUserViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomUserService customUserService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public CustomUserService customUserService() {
            return Mockito.mock(CustomUserService.class);
        }
    }

    @Test
    void listUsers_shouldReturnListView() throws Exception {
        when(customUserService.getFilteredUsers(any(), any(), any(), any()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/users")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("users/list"))
                .andExpect(model().attributeExists("users"));
    }

    @Test
    void showCreateForm_shouldReturnCreateView() throws Exception {
        mockMvc.perform(get("/users/new")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("users/create"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void createUser_shouldRedirectOnSuccess() throws Exception {
        when(customUserService.existsByEmail("test@example.com")).thenReturn(false);
        when(customUserService.existsByName("testuser")).thenReturn(false);
        when(customUserService.existsByPhoneNumber("1234567890")).thenReturn(false);

        mockMvc.perform(post("/users")
                        .param("name", "testuser")
                        .param("email", "test@example.com")
                        .param("phoneNumber", "1234567890")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));
    }

    @Test
    void createUser_shouldReturnCreateViewOnDuplicateEmail() throws Exception {
        when(customUserService.existsByEmail("test@example.com")).thenReturn(true);

        mockMvc.perform(post("/users")
                        .param("name", "testuser")
                        .param("email", "test@example.com")
                        .param("phoneNumber", "1234567890")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("users/create"))
                .andExpect(model().attributeExists("errorMessage"));
    }

    @Test
    void showEditForm_shouldReturnEditView() throws Exception {
        CustomUser user = new CustomUser();
        user.setId(1L);
        user.setName("test");

        when(customUserService.getById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/users/edit/1")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("users/edit"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void updateUser_shouldRedirectOnSuccess() throws Exception {
        when(customUserService.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(customUserService.findByName("testuser")).thenReturn(Optional.empty());
        when(customUserService.findByPhoneNumber("1234567890")).thenReturn(Optional.empty());

        mockMvc.perform(post("/users/update")
                        .param("id", "1")
                        .param("name", "testuser")
                        .param("email", "test@example.com")
                        .param("phoneNumber", "1234567890")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));
    }

    @Test
    void updateUser_shouldReturnEditViewOnDuplicatePhone() throws Exception {
        CustomUser existing = new CustomUser();
        existing.setId(2L);
        when(customUserService.findByPhoneNumber("1234567890")).thenReturn(Optional.of(existing));

        mockMvc.perform(post("/users/update")
                        .param("id", "1")
                        .param("name", "testuser")
                        .param("email", "test@example.com")
                        .param("phoneNumber", "1234567890")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("users/edit"))
                .andExpect(model().attributeExists("errorMessage"));
    }

    @Test
    void deleteUser_shouldRedirect() throws Exception {
        mockMvc.perform(get("/users/delete/1")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));
    }
}
