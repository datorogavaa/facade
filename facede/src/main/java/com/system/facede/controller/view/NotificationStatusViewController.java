package com.system.facede.controller.view;

import com.system.facede.model.NotificationStatus;
import com.system.facede.service.CustomUserService;
import com.system.facede.service.NotificationStatusService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/notification-statuses")
public class NotificationStatusViewController {

    private final NotificationStatusService statusService;
    private final CustomUserService customUserService;
    public NotificationStatusViewController(NotificationStatusService statusService, CustomUserService customUserService) {
        this.statusService = statusService;
        this.customUserService = customUserService;
    }

    @GetMapping
    public String listStatuses(Model model) {
        model.addAttribute("statuses", statusService.getAll());
        return "statuses/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("status", new NotificationStatus());
        model.addAttribute("users", customUserService.getAll()); // add users here!
        return "statuses/create";
    }

    @PostMapping
    public String createStatus(@ModelAttribute NotificationStatus status) {
        statusService.save(status);
        return "redirect:/notification-statuses";
    }


    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        NotificationStatus status = statusService.getById(id).orElseThrow(() -> new RuntimeException("Not found"));
        model.addAttribute("status", status);
        model.addAttribute("users", customUserService.getAll()); // add users here too
        return "statuses/edit";
    }

    @PostMapping("/update/{id}")
    public String updateStatus(@PathVariable Long id, @ModelAttribute NotificationStatus updated) {
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
        return "redirect:/notification-statuses";
    }


    @GetMapping("/delete/{id}")
    public String deleteStatus(@PathVariable Long id) {
        statusService.delete(id);
        return "redirect:/notification-statuses";
    }
}
