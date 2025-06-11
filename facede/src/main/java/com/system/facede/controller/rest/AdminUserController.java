package com.system.facede.controller.rest;
import com.system.facede.model.AdminUser;
import com.system.facede.service.AdminUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin-users")
public class AdminUserController {

    private static final Logger logger = LoggerFactory.getLogger(AdminUserController.class);

    private final AdminUserService adminUserService;

    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @GetMapping
    public List<AdminUser> getAllAdmins() {
        logger.info("Fetching all admin users");
        return adminUserService.getAllAdmins();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAdmin(@PathVariable Long id) {
        logger.info("Fetching admin user with ID: {}", id);
        Optional<AdminUser> admin = adminUserService.getById(id);
        if (admin.isPresent()) {
            return ResponseEntity.ok(admin.get());
        } else {
            logger.warn("Admin user not found with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Admin user not found with ID: " + id);
        }
    }

    @PostMapping
    public AdminUser createAdmin(@RequestBody AdminUser adminUser) {
        logger.info("Creating new admin user");
        return adminUserService.save(adminUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAdmin(@PathVariable Long id) {
        logger.info("Deleting admin user with ID: {}", id);
        Optional<AdminUser> optionalAdmin = adminUserService.getById(id);
        if (optionalAdmin.isPresent()) {
            adminUserService.delete(id);
            return ResponseEntity.ok("Deleted admin successfully");
        } else {
            logger.warn("Admin user not found for deletion with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Admin user not found with ID: " + id);
        }
    }

}