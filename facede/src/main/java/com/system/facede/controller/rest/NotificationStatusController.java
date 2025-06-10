package com.system.facede.controller.rest;

import com.system.facede.model.Address;
import com.system.facede.model.NotificationStatus;
import com.system.facede.service.NotificationStatusService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/statuses")
public class NotificationStatusController {

    private final NotificationStatusService statusService;

    public NotificationStatusController(NotificationStatusService statusService) {
        this.statusService = statusService;
    }

    @GetMapping
    public List<NotificationStatus> getAllStatuses() {
        return statusService.getAll();
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getNotificationStatus(@PathVariable Long id) {
        Optional<NotificationStatus> status = statusService.getById(id);
        if (status.isPresent()) {
            return ResponseEntity.ok(status.get());
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("status with that id does not exist");
        }
    }

    @PostMapping
    public NotificationStatus createStatus(@RequestBody NotificationStatus status) {
        return statusService.save(status);
    }

    @PutMapping("/{id}")
    public NotificationStatus updateStatus(@PathVariable Long id, @RequestBody NotificationStatus updated) {
        Optional<NotificationStatus> optionalNotificationStatus = statusService.getById(id);
        if (optionalNotificationStatus.isPresent()) {
            NotificationStatus existing = optionalNotificationStatus.get();

            // Update fields
            existing.setStatus(updated.getStatus());
            existing.setTimestamp(updated.getTimestamp());
            existing.setChannel(updated.getChannel());
            existing.setMessageId(updated.getMessageId());
            existing.setNote(updated.getNote());
            existing.setCustomUser(updated.getCustomUser());

            return statusService.save(existing);
        } else {
            throw new RuntimeException("Status not found with ID: " + id);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStatus(@PathVariable Long id) {
        Optional<NotificationStatus> optionalStatus = statusService.getById(id);
        if (optionalStatus.isPresent()) {
            statusService.delete(id);
            return ResponseEntity.ok("Status deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Status not found with ID: " + id);
        }
    }

}
