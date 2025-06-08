package com.system.facede.controller.view;

import com.system.facede.model.NotificationPreference;
import com.system.facede.service.CustomUserService;
import com.system.facede.service.NotificationPreferenceService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/notification-preferences")
public class NotificationPreferenceViewController {

    private final NotificationPreferenceService preferenceService;
    private final CustomUserService customUserService;
    public NotificationPreferenceViewController(NotificationPreferenceService preferenceService,CustomUserService customUserService) {
        this.preferenceService = preferenceService;
        this.customUserService = customUserService;
    }

    @GetMapping
    public String listPreferences(Model model) {
        model.addAttribute("preferences", preferenceService.getAll());
        return "preferences/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("preference", new NotificationPreference());
        model.addAttribute("users", customUserService.getAll());
        return "preferences/create";
    }

    @PostMapping("/create")
    public String createPreference(@ModelAttribute NotificationPreference preference) {
        preferenceService.save(preference);
        return "redirect:/notification-preferences";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        NotificationPreference preference = preferenceService.getById(id)
                .orElseThrow(() -> new RuntimeException("Preference not found"));
        model.addAttribute("preference", preference);
        return "preferences/edit";
    }

    @PostMapping("/edit/{id}")
    public String updatePreference(@PathVariable Long id, @ModelAttribute NotificationPreference preferenceFromForm) {
        NotificationPreference existingPreference = preferenceService.getById(id)
                .orElseThrow(() -> new RuntimeException("Preference not found"));

        // Keep the existing CustomUser (do not overwrite)
        preferenceFromForm.setCustomUser(existingPreference.getCustomUser());

        preferenceFromForm.setId(id);
        preferenceService.save(preferenceFromForm);

        return "redirect:/notification-preferences";
    }



    @PostMapping("/delete/{id}")
    public String deletePreference(@PathVariable Long id) {
        preferenceService.delete(id);
        return "redirect:/notification-preferences";
    }
}
