package com.system.facede.controller.rest;

import com.system.facede.dto.NotificationStatusReportDTO;
import com.system.facede.service.NotificationStatusReportingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports/notification-status")
public class NotificationStatusReportingController {

    private static final Logger logger = LoggerFactory.getLogger(NotificationStatusReportingController.class);

    private final NotificationStatusReportingService notificationStatusReportingService;

    public NotificationStatusReportingController(NotificationStatusReportingService notificationStatusReportingService) {
        this.notificationStatusReportingService = notificationStatusReportingService;
    }

    @GetMapping
    public NotificationStatusReportDTO getNotificationStatusReport() {
        logger.info("Fetching notification status report");
        return notificationStatusReportingService.getNotificationStatusReport();
    }
}