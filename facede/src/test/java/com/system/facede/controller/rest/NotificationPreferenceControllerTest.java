package com.system.facede.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.system.facede.controller.rest.NotificationPreferenceController;
import com.system.facede.model.CustomUser;
import com.system.facede.model.NotificationPreference;
import com.system.facede.repository.CustomUserRepository;
import com.system.facede.service.NotificationPreferenceService;
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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationPreferenceController.class)
@Import(NotificationPreferenceControllerTest.TestConfig.class)
class NotificationPreferenceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NotificationPreferenceService preferenceService;

    @Autowired
    private CustomUserRepository customUserRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class TestConfig {
        @Bean public NotificationPreferenceService preferenceService() {
            return Mockito.mock(NotificationPreferenceService.class);
        }
        @Bean public CustomUserRepository customUserRepository() {
            return Mockito.mock(CustomUserRepository.class);
        }
        @Bean public ObjectMapper objectMapper() {
            return new ObjectMapper();
        }
    }

    @Test
    void getAllPreferences_shouldReturnEmptyList() throws Exception {
        when(preferenceService.getAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/preferences")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void getPreference_shouldReturnIfExists() throws Exception {
        NotificationPreference pref = new NotificationPreference();
        pref.setId(1L);
        pref.setEmailEnabled(true);

        when(preferenceService.getById(1L)).thenReturn(Optional.of(pref));

        mockMvc.perform(get("/api/preferences/1")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.emailEnabled").value(true));
    }

    @Test
    void getPreference_shouldReturnNotFoundIfMissing() throws Exception {
        when(preferenceService.getById(2L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/preferences/2")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Preference with that id does not exist"));
    }

    @Test
    void createPreference_shouldBindToUserAndSave() throws Exception {
        CustomUser user = new CustomUser();
        user.setId(1L);

        NotificationPreference input = new NotificationPreference();
        input.setEmailEnabled(true);
        input.setSmsEnabled(false);
        input.setPostalEnabled(true);

        NotificationPreference saved = new NotificationPreference();
        saved.setId(10L);
        saved.setEmailEnabled(true);
        saved.setSmsEnabled(false);
        saved.setPostalEnabled(true);
        saved.setCustomUser(user);

        when(customUserRepository.findById(1L)).thenReturn(Optional.of(user));
        when(preferenceService.save(any())).thenReturn(saved);

        mockMvc.perform(post("/api/preferences/1")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10L));
    }

    @Test
    void updatePreference_shouldModifyAndSave() throws Exception {
        NotificationPreference existing = new NotificationPreference();
        existing.setId(1L);
        existing.setEmailEnabled(false);

        NotificationPreference update = new NotificationPreference();
        update.setEmailEnabled(true);
        update.setSmsEnabled(true);
        update.setPostalEnabled(false);
        update.setCustomUser(new CustomUser());

        when(preferenceService.getById(1L)).thenReturn(Optional.of(existing));
        when(preferenceService.save(any())).thenReturn(existing);

        mockMvc.perform(put("/api/preferences/1")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.emailEnabled").value(true));
    }

    @Test
    void updatePreferencesBatch_shouldReturnSuccess() throws Exception {
        mockMvc.perform(post("/api/preferences/batch")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf())
                        .param("userIds", "1", "2", "3")
                        .param("emailEnabled", "true")
                        .param("smsEnabled", "false")
                        .param("postalEnabled", "true"))
                .andExpect(status().isOk())
                .andExpect(content().string("Batch update successful."));
    }

    @Test
    void createPreference_shouldReturnErrorIfUserNotFound() throws Exception {
        NotificationPreference input = new NotificationPreference();
        input.setEmailEnabled(true);

        when(customUserRepository.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/preferences/99")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Custom user not found with ID: 99"));
    }

}
