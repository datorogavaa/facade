package com.system.facede.controller.view;

import com.system.facede.model.NotificationStatus;
import com.system.facede.service.CustomUserService;
import com.system.facede.service.NotificationStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/notification-statuses")
public class NotificationStatusViewController {

    private static final Logger logger = LoggerFactory.getLogger(NotificationStatusViewController.class);

    private final NotificationStatusService statusService;
    private final CustomUserService customUserService;

    public NotificationStatusViewController(NotificationStatusService statusService, CustomUserService customUserService) {
        this.statusService = statusService;
        this.customUserService = customUserService;
    }

    @GetMapping
    public String listStatuses(Model model) {
        logger.info("Listing all notification statuses");
        model.addAttribute("statuses", statusService.getAll());
        return "statuses/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        logger.info("Showing create notification status form");
        model.addAttribute("status", new NotificationStatus());
        model.addAttribute("users", customUserService.getAll());
        return "statuses/create";
    }

    @PostMapping
    public String createStatus(@ModelAttribute NotificationStatus status) {
        logger.info("Creating notification status for user ID: {}",
                status.getCustomUser() != null ? status.getCustomUser().getId() : null);
        statusService.save(status);
        logger.info("Notification status created successfully");
        return "redirect:/notification-statuses";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        logger.info("Showing edit form for notification status ID: {}", id);
        NotificationStatus status = statusService.getById(id).orElseThrow(() -> new RuntimeException("Not found"));
        model.addAttribute("status", status);
        model.addAttribute("users", customUserService.getAll());
        return "statuses/edit";
    }

    @PostMapping("/update/{id}")
    public String updateStatus(@PathVariable Long id, @ModelAttribute NotificationStatus updated) {
        logger.info("Updating notification status ID: {}", id);
        NotificationStatus existing = statusService.getById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));

        existing.setStatus(updated.getStatus());
        if (updated.getTimestamp() != null) {
            existing.setTimestamp(updated.getTimestamp());
        }
        existing.setCustomUser(updated.getCustomUser());
        existing.setChannel(updated.getChannel());
        existing.setMessageId(updated.getMessageId());
        existing.setNote(updated.getNote());

        statusService.save(existing);
        logger.info("Notification status updated successfully");
        return "redirect:/notification-statuses";
    }

    @GetMapping("/delete/{id}")
    public String deleteStatus(@PathVariable Long id) {
        logger.info("Deleting notification status ID: {}", id);
        statusService.delete(id);
        logger.info("Notification status deleted successfully");
        return "redirect:/notification-statuses";
    }
}