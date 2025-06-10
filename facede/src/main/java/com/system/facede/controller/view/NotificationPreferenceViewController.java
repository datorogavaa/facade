package com.system.facede.controller.view;

import com.system.facede.model.CustomUser;
import com.system.facede.model.NotificationPreference;
import com.system.facede.service.CustomUserService;
import com.system.facede.service.NotificationPreferenceService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/notification-preferences")
public class NotificationPreferenceViewController {

    private final NotificationPreferenceService preferenceService;
    private final CustomUserService customUserService;

    public NotificationPreferenceViewController(NotificationPreferenceService preferenceService, CustomUserService customUserService) {
        this.preferenceService = preferenceService;
        this.customUserService = customUserService;
    }

    @GetMapping
    public String listPreferences(Model model) {
        model.addAttribute("preferences", preferenceService.getAll());
        return "preferences/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("preference", new NotificationPreference());
        model.addAttribute("users", customUserService.getAll());
        return "preferences/create";
    }

    @PostMapping
    public String createPreference(@ModelAttribute NotificationPreference preference) {
        if (preference.getCustomUser() != null && preference.getCustomUser().getId() != null) {
            CustomUser user = customUserService.getById(preference.getCustomUser().getId()).orElse(null);
            preference.setCustomUser(user);
        }
        preferenceService.save(preference);
        return "redirect:/notification-preferences";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        NotificationPreference preference = preferenceService.getById(id)
                .orElseThrow(() -> new RuntimeException("Preference not found"));
        model.addAttribute("preference", preference);
        model.addAttribute("users", customUserService.getAll());
        return "preferences/edit";
    }

    @PostMapping("/update")
    public String updatePreference(@ModelAttribute NotificationPreference preference) {
        if (preference.getCustomUser() != null && preference.getCustomUser().getId() != null) {
            CustomUser user = customUserService.getById(preference.getCustomUser().getId()).orElse(null);
            preference.setCustomUser(user);
        }
        preferenceService.save(preference);
        return "redirect:/notification-preferences";
    }
}