package com.system.facede.controller.rest;

import com.system.facede.dto.NotificationStatusReportDTO;
import com.system.facede.service.NotificationStatusReportingService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationStatusReportingController.class)
@Import(NotificationStatusReportingControllerTest.TestConfig.class)
class NotificationStatusReportingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NotificationStatusReportingService reportingService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public NotificationStatusReportingService reportingService() {
            return Mockito.mock(NotificationStatusReportingService.class);
        }
    }

    @Test
    void getNotificationStatusReport_shouldReturnFullReport() throws Exception {
        NotificationStatusReportDTO report = new NotificationStatusReportDTO(
                100L, 10L, 5L,    // SMS: delivered, failed, pending
                200L, 20L, 10L,   // Email: delivered, failed, pending
                300L, 30L, 15L    // Postal: delivered, failed, pending
        );

        when(reportingService.getNotificationStatusReport()).thenReturn(report);

        mockMvc.perform(get("/api/reports/notification-status")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deliveredSmsCount").value(100))
                .andExpect(jsonPath("$.failedSmsCount").value(10))
                .andExpect(jsonPath("$.pendingSmsCount").value(5))
                .andExpect(jsonPath("$.deliveredEmailCount").value(200))
                .andExpect(jsonPath("$.failedEmailCount").value(20))
                .andExpect(jsonPath("$.pendingEmailCount").value(10))
                .andExpect(jsonPath("$.deliveredPostalCount").value(300))
                .andExpect(jsonPath("$.failedPostalCount").value(30))
                .andExpect(jsonPath("$.pendingPostalCount").value(15));
    }
}
