package com.system.facede.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.system.facede.model.CustomUser;
import com.system.facede.model.NotificationStatus;
import com.system.facede.service.NotificationStatusService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationStatusController.class)
@Import(NotificationStatusControllerTest.Config.class)
class NotificationStatusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NotificationStatusService statusService;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class Config {
        @Bean
        public NotificationStatusService statusService() {
            return Mockito.mock(NotificationStatusService.class);
        }

        @Bean
        public ObjectMapper objectMapper() {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            return mapper;
        }
    }

    @Test
    void getAllStatuses_shouldReturnEmptyList() throws Exception {
        when(statusService.getAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/statuses")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void getNotificationStatus_shouldReturnIfExists() throws Exception {
        NotificationStatus status = new NotificationStatus();
        status.setId(1L);
        status.setStatus("DELIVERED");

        when(statusService.getById(1L)).thenReturn(Optional.of(status));

        mockMvc.perform(get("/api/statuses/1")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DELIVERED"));
    }

    @Test
    void getNotificationStatus_shouldReturnNotFound() throws Exception {
        when(statusService.getById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/statuses/999")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isNotFound())
                .andExpect(content().string("status with that id does not exist"));
    }

    @Test
    void createStatus_shouldReturnCreatedStatus() throws Exception {
        NotificationStatus input = new NotificationStatus();
        input.setStatus("PENDING");

        NotificationStatus saved = new NotificationStatus();
        saved.setId(3L);
        saved.setStatus("PENDING");

        when(statusService.save(any())).thenReturn(saved);

        mockMvc.perform(post("/api/statuses")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3L))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void updateStatus_shouldModifyAndReturn() throws Exception {
        NotificationStatus existing = new NotificationStatus();
        existing.setId(1L);
        existing.setStatus("PENDING");

        NotificationStatus updated = new NotificationStatus();
        updated.setStatus("FAILED");
        updated.setTimestamp(LocalDateTime.now());
        updated.setChannel("SMS");
        updated.setMessageId("MSG001");
        updated.setNote("Delivery failed");
        updated.setCustomUser(new CustomUser());

        when(statusService.getById(1L)).thenReturn(Optional.of(existing));
        when(statusService.save(any())).thenReturn(existing);

        mockMvc.perform(put("/api/statuses/1")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("FAILED"));
    }

    @Test
    void deleteStatus_shouldReturnSuccessIfExists() throws Exception {
        NotificationStatus existing = new NotificationStatus();
        existing.setId(2L);

        when(statusService.getById(2L)).thenReturn(Optional.of(existing));
        doNothing().when(statusService).delete(2L);

        mockMvc.perform(delete("/api/statuses/2")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Status deleted successfully"));
    }

    @Test
    void deleteStatus_shouldReturnNotFoundIfMissing() throws Exception {
        when(statusService.getById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/statuses/999")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Status not found with ID: 999"));
    }
}
