package com.system.facede.controller.view;

import com.system.facede.model.CustomUser;
import com.system.facede.model.NotificationPreference;
import com.system.facede.service.CustomUserService;
import com.system.facede.service.NotificationPreferenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/notification-preferences")
public class NotificationPreferenceViewController {

    private static final Logger logger = LoggerFactory.getLogger(NotificationPreferenceViewController.class);

    private final NotificationPreferenceService preferenceService;
    private final CustomUserService customUserService;

    public NotificationPreferenceViewController(NotificationPreferenceService preferenceService, CustomUserService customUserService) {
        this.preferenceService = preferenceService;
        this.customUserService = customUserService;
    }

    @GetMapping
    public String listPreferences(Model model) {
        logger.info("Listing all notification preferences");
        model.addAttribute("preferences", preferenceService.getAll());
        return "preferences/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        logger.info("Showing create notification preference form");
        List<CustomUser> allUsers = customUserService.getAll();
        List<NotificationPreference> existingPreferences = preferenceService.getAll();

        Set<Long> usedUserIds = existingPreferences.stream()
                .filter(pref -> pref.getCustomUser() != null)
                .map(pref -> pref.getCustomUser().getId())
                .collect(Collectors.toSet());

        List<CustomUser> availableUsers = allUsers.stream()
                .filter(user -> !usedUserIds.contains(user.getId()))
                .collect(Collectors.toList());

        model.addAttribute("preference", new NotificationPreference());
        model.addAttribute("users", availableUsers);
        return "preferences/create";
    }

    @PostMapping
    public String createPreference(@ModelAttribute NotificationPreference preference) {
        logger.info("Creating notification preference for user ID: {}",
                preference.getCustomUser() != null ? preference.getCustomUser().getId() : null);
        if (preference.getCustomUser() != null && preference.getCustomUser().getId() != null) {
            CustomUser user = customUserService.getById(preference.getCustomUser().getId()).orElse(null);
            preference.setCustomUser(user);
        }
        preferenceService.save(preference);
        logger.info("Notification preference created successfully");
        return "redirect:/notification-preferences";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        logger.info("Showing edit form for notification preference ID: {}", id);
        NotificationPreference preference = preferenceService.getById(id)
                .orElseThrow(() -> new RuntimeException("Preference not found"));
        model.addAttribute("preference", preference);
        model.addAttribute("users", customUserService.getAll());
        return "preferences/edit";
    }

    @PostMapping("/update")
    public String updatePreference(@ModelAttribute NotificationPreference preference) {
        logger.info("Updating notification preference ID: {}", preference.getId());
        if (preference.getCustomUser() != null && preference.getCustomUser().getId() != null) {
            CustomUser user = customUserService.getById(preference.getCustomUser().getId()).orElse(null);
            preference.setCustomUser(user);
        }
        preferenceService.save(preference);
        logger.info("Notification preference updated successfully");
        return "redirect:/notification-preferences";
    }
}