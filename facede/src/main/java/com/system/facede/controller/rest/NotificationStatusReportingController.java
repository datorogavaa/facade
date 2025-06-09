package com.system.facede.controller.rest;

import com.system.facede.dto.NotificationStatusReportDTO;
import com.system.facede.service.NotificationStatusReportingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports/notification-status")
public class NotificationStatusReportingController {

    private final NotificationStatusReportingService notificationStatusReportingService;

    public NotificationStatusReportingController(NotificationStatusReportingService notificationStatusReportingService) {
        this.notificationStatusReportingService = notificationStatusReportingService;
    }

    @GetMapping
    public NotificationStatusReportDTO getNotificationStatusReport() {
        return notificationStatusReportingService.getNotificationStatusReport();
    }
}
