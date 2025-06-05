package com.system.facede.controller.view;

import com.system.facede.model.NotificationPreference;
import com.system.facede.service.NotificationPreferenceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/notification-preferences")
public class NotificationPreferenceViewController {

    private final NotificationPreferenceService preferenceService;

    public NotificationPreferenceViewController(NotificationPreferenceService preferenceService) {
        this.preferenceService = preferenceService;
    }

    @GetMapping
    public String listPreferences(Model model) {
        model.addAttribute("preferences", preferenceService.getAll());
        return "notification-preferences/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("preference", new NotificationPreference());
        return "notification-preferences/create";
    }

    @PostMapping
    public String createPreference(@ModelAttribute NotificationPreference preference) {
        preferenceService.save(preference);
        return "redirect:/notification-preferences";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        NotificationPreference preference = preferenceService.getById(id)
                .orElseThrow(() -> new RuntimeException("Preference not found"));
        model.addAttribute("preference", preference);
        return "notification-preferences/edit";
    }

    @PostMapping("/edit/{id}")
    public String updatePreference(@ModelAttribute NotificationPreference preference) {
        preferenceService.save(preference);
        return "redirect:/notification-preferences";
    }


    @PostMapping("/delete/{id}")
    public String deletePreference(@PathVariable Long id) {
        preferenceService.delete(id);
        return "redirect:/notification-preferences";
    }
}
