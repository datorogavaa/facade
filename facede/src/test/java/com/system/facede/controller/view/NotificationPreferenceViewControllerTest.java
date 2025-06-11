package com.system.facede.controller.view;

import com.system.facede.model.CustomUser;
import com.system.facede.model.NotificationPreference;
import com.system.facede.service.CustomUserService;
import com.system.facede.service.NotificationPreferenceService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationPreferenceViewController.class)
@Import(NotificationPreferenceViewControllerTest.TestConfig.class)
class NotificationPreferenceViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NotificationPreferenceService preferenceService;

    @Autowired
    private CustomUserService customUserService;

    @TestConfiguration
    static class TestConfig {
        @Bean public NotificationPreferenceService preferenceService() { return Mockito.mock(NotificationPreferenceService.class); }
        @Bean public CustomUserService customUserService() { return Mockito.mock(CustomUserService.class); }
    }

    @Test
    void listPreferences_shouldReturnListView() throws Exception {
        when(preferenceService.getAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/notification-preferences").with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("preferences/list"))
                .andExpect(model().attributeExists("preferences"));
    }

    @Test
    void showCreateForm_shouldReturnCreateView() throws Exception {
        CustomUser user = new CustomUser();
        user.setId(1L);
        when(customUserService.getAll()).thenReturn(List.of(user));
        when(preferenceService.getAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/notification-preferences/create").with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("preferences/create"))
                .andExpect(model().attributeExists("preference"))
                .andExpect(model().attributeExists("users"));
    }

    @Test
    void createPreference_shouldRedirectOnSuccess() throws Exception {
        CustomUser user = new CustomUser();
        user.setId(1L);
        when(customUserService.getById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(post("/notification-preferences")
                        .param("customUser.id", "1")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/notification-preferences"));
    }

    @Test
    void showEditForm_shouldReturnEditView() throws Exception {
        NotificationPreference pref = new NotificationPreference();
        pref.setId(1L);
        when(preferenceService.getById(1L)).thenReturn(Optional.of(pref));
        when(customUserService.getAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/notification-preferences/edit/1").with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("preferences/edit"))
                .andExpect(model().attributeExists("preference"))
                .andExpect(model().attributeExists("users"));
    }

    @Test
    void updatePreference_shouldRedirect() throws Exception {
        CustomUser user = new CustomUser();
        user.setId(1L);
        when(customUserService.getById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(post("/notification-preferences/update")
                        .param("id", "1")
                        .param("customUser.id", "1")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/notification-preferences"));
    }
}
