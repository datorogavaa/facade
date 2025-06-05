package com.system.facede.controller.view;

import com.system.facede.model.NotificationStatus;
import com.system.facede.service.NotificationStatusService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/statuses")
public class NotificationStatusViewController {

    private final NotificationStatusService statusService;

    public NotificationStatusViewController(NotificationStatusService statusService) {
        this.statusService = statusService;
    }

    @GetMapping
    public String listStatuses(Model model) {
        model.addAttribute("statuses", statusService.getAll());
        return "statuses/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("status", new NotificationStatus());
        return "statuses/create";
    }

    @PostMapping
    public String createStatus(@ModelAttribute NotificationStatus status) {
        statusService.save(status);
        return "redirect:/statuses";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        NotificationStatus status = statusService.getById(id).orElseThrow(() -> new RuntimeException("Not found"));
        model.addAttribute("status", status);
        return "statuses/edit";
    }

    @PostMapping("/update/{id}")
    public String updateStatus(@PathVariable Long id, @ModelAttribute NotificationStatus updated) {
        NotificationStatus existing = statusService.getById(id).orElseThrow(() -> new RuntimeException("Not found"));
        existing.setStatus(updated.getStatus());
        existing.setTimestamp(updated.getTimestamp());
        existing.setCustomUser(updated.getCustomUser());
        statusService.save(existing);
        return "redirect:/statuses";
    }

    @GetMapping("/delete/{id}")
    public String deleteStatus(@PathVariable Long id) {
        statusService.delete(id);
        return "redirect:/statuses";
    }
}
