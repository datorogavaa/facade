package com.system.facede.controller.rest;

import com.system.facede.model.NotificationStatus;
import com.system.facede.service.NotificationStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/statuses")
public class NotificationStatusController {

    private static final Logger logger = LoggerFactory.getLogger(NotificationStatusController.class);

    private final NotificationStatusService statusService;

    public NotificationStatusController(NotificationStatusService statusService) {
        this.statusService = statusService;
    }

    @GetMapping
    public List<NotificationStatus> getAllStatuses() {
        logger.info("Fetching all notification statuses");
        return statusService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getNotificationStatus(@PathVariable Long id) {
        logger.info("Fetching notification status with ID: {}", id);
        Optional<NotificationStatus> status = statusService.getById(id);
        if (status.isPresent()) {
            return ResponseEntity.ok(status.get());
        } else {
            logger.warn("Notification status not found with ID: {}", id);
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("status with that id does not exist");
        }
    }

    @PostMapping
    public NotificationStatus createStatus(@RequestBody NotificationStatus status) {
        logger.info("Creating new notification status");
        return statusService.save(status);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody NotificationStatus updated) {
        logger.info("Updating notification status with ID: {}", id);
        Optional<NotificationStatus> optionalNotificationStatus = statusService.getById(id);

        if (optionalNotificationStatus.isPresent()) {
            NotificationStatus existing = optionalNotificationStatus.get();

            existing.setStatus(updated.getStatus());
            existing.setTimestamp(updated.getTimestamp());
            existing.setChannel(updated.getChannel());
            existing.setMessageId(updated.getMessageId());
            existing.setNote(updated.getNote());
            existing.setCustomUser(updated.getCustomUser());

            NotificationStatus saved = statusService.save(existing);
            return ResponseEntity.ok(saved);
        } else {
            logger.warn("Notification status not found for update with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Status not found with ID: " + id);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStatus(@PathVariable Long id) {
        logger.info("Deleting notification status with ID: {}", id);
        Optional<NotificationStatus> optionalStatus = statusService.getById(id);
        if (optionalStatus.isPresent()) {
            statusService.delete(id);
            return ResponseEntity.ok("Status deleted successfully");
        } else {
            logger.warn("Notification status not found for deletion with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Status not found with ID: " + id);
        }
    }

}