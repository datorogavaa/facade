package com.system.facede.controller.view;

import com.system.facede.dto.NotificationOptInReportDTO;
import com.system.facede.service.NotificationOptInReportingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NotificationOptInReportingViewController {

    private static final Logger logger = LoggerFactory.getLogger(NotificationOptInReportingViewController.class);

    private final NotificationOptInReportingService notificationOptInReportingService;

    public NotificationOptInReportingViewController(NotificationOptInReportingService notificationOptInReportingService) {
        this.notificationOptInReportingService = notificationOptInReportingService;
    }

    @GetMapping("/reports/notification-opt-ins")
    public String showNotificationOptInReport(Model model) {
        logger.info("Fetching notification opt-in report");
        NotificationOptInReportDTO report = notificationOptInReportingService.getNotificationOptInReport();
        model.addAttribute("report", report);
        return "reports/notification-opt-ins";
    }
}