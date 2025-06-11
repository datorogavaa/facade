package com.system.facede.controller.rest;

import com.system.facede.dto.NotificationOptInReportDTO;
import com.system.facede.service.NotificationOptInReportingService;
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

@WebMvcTest(NotificationOptInReportingController.class)
@Import(NotificationOptInReportingControllerTest.TestConfig.class)
class NotificationOptInReportingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NotificationOptInReportingService reportingService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public NotificationOptInReportingService reportingService() {
            return Mockito.mock(NotificationOptInReportingService.class);
        }
    }

    @Test
    void getNotificationOptInReport_shouldReturnCorrectCounts() throws Exception {
        NotificationOptInReportDTO report =
                new NotificationOptInReportDTO(120L, 85L, 60L);

        when(reportingService.getNotificationOptInReport()).thenReturn(report);

        mockMvc.perform(get("/api/reports/notification-opt-ins")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.emailOptInCount").value(120))
                .andExpect(jsonPath("$.smsOptInCount").value(85))
                .andExpect(jsonPath("$.postalOptInCount").value(60));
    }
}
