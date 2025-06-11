package com.system.facede.controller.view;

import com.system.facede.dto.NotificationOptInReportDTO;
import com.system.facede.service.NotificationOptInReportingService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationOptInReportingViewController.class)
@Import(NotificationOptInReportingViewControllerTest.TestConfig.class)
class NotificationOptInReportingViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NotificationOptInReportingService reportService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public NotificationOptInReportingService reportService() {
            return Mockito.mock(NotificationOptInReportingService.class);
        }
    }

    @Test
    void showNotificationOptInReport_shouldReturnReportView() throws Exception {
        NotificationOptInReportDTO mockReport = new NotificationOptInReportDTO(5L, 3L, 2L);
        when(reportService.getNotificationOptInReport()).thenReturn(mockReport);

        mockMvc.perform(get("/reports/notification-opt-ins")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("reports/notification-opt-ins"))
                .andExpect(model().attributeExists("report"));
    }
}
