package com.system.facede.controller.view;

import com.system.facede.dto.NotificationStatusReportDTO;
import com.system.facede.service.NotificationStatusReportingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NotificationStatusReportingViewController {

    private final NotificationStatusReportingService notificationStatusReportingService;

    public NotificationStatusReportingViewController(NotificationStatusReportingService notificationStatusReportingService) {
        this.notificationStatusReportingService = notificationStatusReportingService;
    }

    @GetMapping("/reports/notification-status")
    public String getNotificationStatusReport(Model model) {
        NotificationStatusReportDTO report = notificationStatusReportingService.getNotificationStatusReport();

        model.addAttribute("report", report);

        return "/reports/notification-status-report";
    }
}
