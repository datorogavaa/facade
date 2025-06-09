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

        // Add the report data to the model to pass it to Thymeleaf
        model.addAttribute("report", report);

        // Return the view name to be rendered (Thymeleaf template)
        return "/reports/notification-status-report"; // Name of the HTML template file (notification-status-report.html)
    }
}
