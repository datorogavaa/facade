package com.system.facede.controller.rest;

import com.system.facede.dto.NotificationOptInReportDTO;
import com.system.facede.service.NotificationOptInReportingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
public class NotificationOptInReportingController {

    private final NotificationOptInReportingService notificationOptInReportingService;

    public NotificationOptInReportingController(NotificationOptInReportingService notificationOptInReportingService) {
        this.notificationOptInReportingService = notificationOptInReportingService;
    }

    @GetMapping("/notification-opt-ins")
    public NotificationOptInReportDTO getNotificationOptInReport() {
        return notificationOptInReportingService.getNotificationOptInReport();
    }
}
