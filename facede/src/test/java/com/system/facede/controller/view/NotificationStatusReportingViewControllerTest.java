package com.system.facede.controller.view;

import com.system.facede.dto.NotificationStatusReportDTO;
import com.system.facede.service.NotificationStatusReportingService;
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

@WebMvcTest(NotificationStatusReportingViewController.class)
@Import(NotificationStatusReportingViewControllerTest.TestConfig.class)
class NotificationStatusReportingViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NotificationStatusReportingService statusService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public NotificationStatusReportingService notificationStatusReportingService() {
            return Mockito.mock(NotificationStatusReportingService.class);
        }
    }

    @Test
    void getNotificationStatusReport_shouldReturnReportViewWithAllAttributes() throws Exception {
        NotificationStatusReportDTO mockReport = new NotificationStatusReportDTO(
                10L, 2L, 1L,   // SMS
                8L, 3L, 2L,    // Email
                5L, 0L, 4L     // Postal
        );

        when(statusService.getNotificationStatusReport()).thenReturn(mockReport);

        mockMvc.perform(get("/reports/notification-status").with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("/reports/notification-status-report"))
                .andExpect(model().attributeExists("statusReport"))
                .andExpect(model().attribute("deliveredSmsCount", 10L))
                .andExpect(model().attribute("failedSmsCount", 2L))
                .andExpect(model().attribute("pendingSmsCount", 1L))
                .andExpect(model().attribute("deliveredEmailCount", 8L))
                .andExpect(model().attribute("failedEmailCount", 3L))
                .andExpect(model().attribute("pendingEmailCount", 2L))
                .andExpect(model().attribute("deliveredPostalCount", 5L))
                .andExpect(model().attribute("failedPostalCount", 0L))
                .andExpect(model().attribute("pendingPostalCount", 4L));
    }

}
