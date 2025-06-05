package com.system.facede.controller.rest;

import com.system.facede.model.NotificationStatus;
import com.system.facede.service.NotificationStatusService;
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

    @PostMapping
    public NotificationStatus createStatus(@RequestBody NotificationStatus status) {
        return statusService.save(status);
    }

    @PutMapping("/{id}")
    public NotificationStatus updateStatus(@PathVariable Long id, @RequestBody NotificationStatus updated) {
        Optional<NotificationStatus> optionalNotificationStatus = statusService.getById(id);
        if (optionalNotificationStatus.isPresent()) {
            NotificationStatus existing = optionalNotificationStatus.get();
            existing.setStatus(updated.getStatus());
            existing.setTimestamp(updated.getTimestamp());
            existing.setCustomUser(updated.getCustomUser());
            return statusService.save(existing);
        } else {
            throw new RuntimeException("Status not found with ID: " + id);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteStatus(@PathVariable Long id) {
        statusService.delete(id);
    }
}
