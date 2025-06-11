package com.system.facede.controller.rest;

import com.system.facede.model.CustomUser;
import com.system.facede.model.NotificationPreference;
import com.system.facede.repository.CustomUserRepository;
import com.system.facede.service.NotificationPreferenceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/preferences")
public class NotificationPreferenceController {

    private final NotificationPreferenceService preferenceService;
    private final CustomUserRepository customUserRepository;

    public NotificationPreferenceController(NotificationPreferenceService preferenceService, CustomUserRepository customUserRepository) {
        this.preferenceService = preferenceService;
        this.customUserRepository = customUserRepository;
    }

    @GetMapping
    public List<NotificationPreference> getAllPreferences() {
        return preferenceService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPreference(@PathVariable Long id) {
        Optional<NotificationPreference> pref = preferenceService.getById(id);
        if(pref.isPresent()) {
            return  ResponseEntity.ok(pref.get());
        }else{
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                    .body("Preference with that id does not exist");
        }
    }


    @PostMapping("/{userId}")
    public NotificationPreference createPreference(@PathVariable Long userId, @RequestBody NotificationPreference pref) {
        CustomUser user = customUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        pref.setCustomUser(user);
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

    @PostMapping("/batch")
    public ResponseEntity<String> updatePreferencesBatch(
            @RequestParam List<Long> userIds,
            @RequestParam boolean emailEnabled,
            @RequestParam boolean smsEnabled,
            @RequestParam boolean postalEnabled
    ) {
        try {
            preferenceService.updateNotificationPreferencesBatch(userIds, emailEnabled, smsEnabled, postalEnabled);
            return ResponseEntity.ok("Batch update successful.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

}
