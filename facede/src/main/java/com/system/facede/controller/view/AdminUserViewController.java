package com.system.facede.controller.view;

import com.system.facede.model.AdminUser;
import com.system.facede.service.AdminUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admins")
public class AdminUserViewController {

    private final AdminUserService adminUserService;

    public AdminUserViewController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @GetMapping
    public String listAdmins(Model model) {
        model.addAttribute("admins", adminUserService.getAllAdmins());
        return "admins/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("admin", new AdminUser());
        return "admins/create";
    }

    @PostMapping
    public String createAdmin(@ModelAttribute AdminUser adminUser) {
        adminUserService.save(adminUser);
        return "redirect:/admins";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        adminUserService.getById(id).ifPresent(admin -> model.addAttribute("admin", admin));
        return "admins/edit";
    }

    @PostMapping("/update")
    public String updateAdmin(@ModelAttribute AdminUser adminUser) {
        adminUserService.save(adminUser);
        return "redirect:/admins";
    }

    @GetMapping("/delete/{id}")
    public String deleteAdmin(@PathVariable Long id) {
        adminUserService.delete(id);
        return "redirect:/admins";
    }
}