package com.system.facede.controller.rest;

import com.system.facede.model.AdminUser;
import com.system.facede.service.AdminUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin-users")
public class AdminUserController {

    private final AdminUserService adminUserService;

    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @GetMapping
    public List<AdminUser> getAllAdmins() {
        return adminUserService.getAllAdmins();
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getAdmin(@PathVariable Long id) {
        Optional<AdminUser> admin = adminUserService.getById(id);
        if (admin.isPresent()) {
            return ResponseEntity.ok(admin.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Admin user not found with ID: " + id);
        }
    }

    @PostMapping
    public AdminUser createAdmin(@RequestBody AdminUser adminUser) {
        return adminUserService.save(adminUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAdmin(@PathVariable Long id) {
        Optional<AdminUser> optionalAdmin = adminUserService.getById(id);
        if (optionalAdmin.isPresent()) {
            adminUserService.delete(id);
            return ResponseEntity.ok("Deleted admin successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Admin user not found with ID: " + id);
        }
    }

}
