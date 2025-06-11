package com.system.facede.controller.view;

import com.system.facede.model.CustomUser;
import com.system.facede.model.NotificationStatus;
import com.system.facede.service.CustomUserService;
import com.system.facede.service.NotificationStatusService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationStatusViewController.class)
@Import(NotificationStatusViewControllerTest.TestConfig.class)
class NotificationStatusViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NotificationStatusService statusService;

    @Autowired
    private CustomUserService customUserService;

    @TestConfiguration
    static class TestConfig {
        @Bean public NotificationStatusService statusService() { return Mockito.mock(NotificationStatusService.class); }
        @Bean public CustomUserService customUserService() { return Mockito.mock(CustomUserService.class); }
    }

    @Test
    void listStatuses_shouldReturnListView() throws Exception {
        when(statusService.getAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/notification-statuses").with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("statuses/list"))
                .andExpect(model().attributeExists("statuses"));
    }

    @Test
    void showCreateForm_shouldReturnCreateView() throws Exception {
        when(customUserService.getAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/notification-statuses/new").with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("statuses/create"))
                .andExpect(model().attributeExists("status"))
                .andExpect(model().attributeExists("users"));
    }

    @Test
    void createStatus_shouldRedirectOnSuccess() throws Exception {
        mockMvc.perform(post("/notification-statuses")
                        .param("status", "DELIVERED")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/notification-statuses"));
    }

    @Test
    void showEditForm_shouldReturnEditView() throws Exception {
        NotificationStatus status = new NotificationStatus();
        status.setId(1L);
        status.setStatus("PENDING");

        when(statusService.getById(1L)).thenReturn(Optional.of(status));
        when(customUserService.getAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/notification-statuses/edit/1").with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("statuses/edit"))
                .andExpect(model().attributeExists("status"))
                .andExpect(model().attributeExists("users"));
    }

    @Test
    void updateStatus_shouldRedirectOnSuccess() throws Exception {
        NotificationStatus existing = new NotificationStatus();
        existing.setId(1L);
        existing.setStatus("PENDING");

        CustomUser user = new CustomUser();
        user.setId(2L);

        when(statusService.getById(1L)).thenReturn(Optional.of(existing));

        mockMvc.perform(post("/notification-statuses/update/1")
                        .param("status", "FAILED")
                        .param("customUser.id", "2")
                        .param("channel", "SMS")
                        .param("messageId", "MSG123")
                        .param("note", "Updated note")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/notification-statuses"));
    }

    @Test
    void deleteStatus_shouldRedirectOnSuccess() throws Exception {
        mockMvc.perform(get("/notification-statuses/delete/1")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/notification-statuses"));
    }
}
