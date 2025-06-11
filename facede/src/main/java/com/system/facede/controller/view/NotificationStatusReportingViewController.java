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
        NotificationStatusReportDTO statusReport = notificationStatusReportingService.getNotificationStatusReport();

        model.addAttribute("statusReport", statusReport);

        // Also add individual attributes for easier Thymeleaf binding
        model.addAttribute("deliveredSmsCount", statusReport.getDeliveredSmsCount());
        model.addAttribute("failedSmsCount", statusReport.getFailedSmsCount());
        model.addAttribute("pendingSmsCount", statusReport.getPendingSmsCount());

        model.addAttribute("deliveredEmailCount", statusReport.getDeliveredEmailCount());
        model.addAttribute("failedEmailCount", statusReport.getFailedEmailCount());
        model.addAttribute("pendingEmailCount", statusReport.getPendingEmailCount());

        model.addAttribute("deliveredPostalCount", statusReport.getDeliveredPostalCount());
        model.addAttribute("failedPostalCount", statusReport.getFailedPostalCount());
        model.addAttribute("pendingPostalCount", statusReport.getPendingPostalCount());

        return "/reports/notification-status-report";
    }
}
