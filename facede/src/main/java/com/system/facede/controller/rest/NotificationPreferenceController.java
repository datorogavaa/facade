package com.system.facede.controller.rest;

import com.system.facede.model.CustomUser;
import com.system.facede.model.NotificationPreference;
import com.system.facede.repository.CustomUserRepository;
import com.system.facede.service.NotificationPreferenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/preferences")
public class NotificationPreferenceController {

    private static final Logger logger = LoggerFactory.getLogger(NotificationPreferenceController.class);

    private final NotificationPreferenceService preferenceService;
    private final CustomUserRepository customUserRepository;

    public NotificationPreferenceController(NotificationPreferenceService preferenceService, CustomUserRepository customUserRepository) {
        this.preferenceService = preferenceService;
        this.customUserRepository = customUserRepository;
    }

    @GetMapping
    public List<NotificationPreference> getAllPreferences() {
        logger.info("Fetching all notification preferences");
        return preferenceService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPreference(@PathVariable Long id) {
        logger.info("Fetching notification preference with ID: {}", id);
        Optional<NotificationPreference> pref = preferenceService.getById(id);
        if(pref.isPresent()) {
            return  ResponseEntity.ok(pref.get());
        }else{
            logger.warn("Notification preference not found with ID: {}", id);
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Preference with that id does not exist");
        }
    }

    @PostMapping("/{userId}")
    public ResponseEntity<?> createPreference(@PathVariable Long userId, @RequestBody NotificationPreference pref) {
        logger.info("Creating notification preference for user ID: {}", userId);
        Optional<CustomUser> userOpt = customUserRepository.findById(userId);

        if (userOpt.isEmpty()) {
            logger.warn("Custom user not found with ID: {}", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Custom user not found with ID: " + userId);
        }

        pref.setCustomUser(userOpt.get());
        NotificationPreference saved = preferenceService.save(pref);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePreference(@PathVariable Long id, @RequestBody NotificationPreference updated) {
        logger.info("Updating notification preference with ID: {}", id);
        Optional<NotificationPreference> optionalPreference = preferenceService.getById(id);
        if (optionalPreference.isPresent()) {
            NotificationPreference existing = optionalPreference.get();
            existing.setEmailEnabled(updated.isEmailEnabled());
            existing.setSmsEnabled(updated.isSmsEnabled());
            existing.setPostalEnabled(updated.isPostalEnabled());
            existing.setCustomUser(updated.getCustomUser());

            NotificationPreference saved = preferenceService.save(existing);
            return ResponseEntity.ok(saved);
        } else {
            logger.warn("Notification preference not found for update with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Preference not found with ID: " + id);
        }
    }

    @PostMapping("/batch")
    public ResponseEntity<String> updatePreferencesBatch(
            @RequestParam List<Long> userIds,
            @RequestParam boolean emailEnabled,
            @RequestParam boolean smsEnabled,
            @RequestParam boolean postalEnabled
    ) {
        logger.info("Batch updating notification preferences for user IDs: {}", userIds);
        try {
            preferenceService.updateNotificationPreferencesBatch(userIds, emailEnabled, smsEnabled, postalEnabled);
            return ResponseEntity.ok("Batch update successful.");
        } catch (IllegalArgumentException e) {
            logger.error("Batch update failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

}