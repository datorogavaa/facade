package com.system.facede.controller.rest;

import com.system.facede.model.AdminUser;
import com.system.facede.service.AdminUserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping
    public AdminUser createAdmin(@RequestBody AdminUser adminUser) {
        return adminUserService.save(adminUser);
    }

    @DeleteMapping("/{id}")
    public void deleteAdmin(@PathVariable Long id) {
        adminUserService.delete(id);
    }
}
