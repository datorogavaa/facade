package com.system.facede.controller.rest;

import com.system.facede.model.NotificationPreference;
import com.system.facede.service.NotificationPreferenceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/preferences")
public class NotificationPreferenceController {

    private final NotificationPreferenceService preferenceService;

    public NotificationPreferenceController(NotificationPreferenceService preferenceService) {
        this.preferenceService = preferenceService;
    }

    @GetMapping
    public List<NotificationPreference> getAllPreferences() {
        return preferenceService.getAll();
    }

    @PostMapping
    public NotificationPreference createPreference(@RequestBody NotificationPreference pref) {
        return preferenceService.save(pref);
    }

    @PutMapping("/{id}")
    public NotificationPreference updatePreference(@PathVariable Long id, @RequestBody NotificationPreference updated) {
        Optional<NotificationPreference> optionalPreference = preferenceService.getById(id);
        if (optionalPreference.isPresent()) {
            NotificationPreference existing = optionalPreference.get();
            existing.setEmailEnabled(updated.isEmailEnabled());
            existing.setSmsEnabled(updated.isSmsEnabled());
            existing.setPostalEnabled(updated.isPostalEnabled());
            existing.setCustomUser(updated.getCustomUser());
            return preferenceService.save(existing);
        } else {
            throw new RuntimeException("Preference not found with ID: " + id);
        }
    }


    @DeleteMapping("/{id}")
    public void deletePreference(@PathVariable Long id) {
        preferenceService.delete(id);
    }
}
