package com.system.facede.controller.view;

import com.system.facede.exception.PasswordEmptyException;
import com.system.facede.exception.UsernameAlreadyExistsException;
import com.system.facede.exception.UsernameEmptyException;
import com.system.facede.model.AdminUser;
import com.system.facede.service.AdminUserService;
import org.springframework.context.annotation.Role;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminUserViewController {

    private final AdminUserService adminUserService;

    public AdminUserViewController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @GetMapping("/list")
    public String listAdmins(Model model) {
        model.addAttribute("admins", adminUserService.getAllAdmins());
        return "admin/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("admin", new AdminUser());
        return "admin/create";
    }

    @PostMapping("/create")
    public String createAdmin(@ModelAttribute AdminUser adminUser, Model model) {
        try {
            adminUserService.save(adminUser);
            return "redirect:/admin/dashboard";
        } catch (UsernameAlreadyExistsException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("admin", adminUser); // Keeps input filled

            return "admin/create"; // Stay on form if error
        }  catch (PasswordEmptyException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("admin", adminUser);
            return "admin/create";
        } catch (UsernameEmptyException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("admin", adminUser);
            return "admin/create";
        }
    }


    @GetMapping("/login")
    public String showLoginForm() {
        return "admin/login";  // this returns your Thymeleaf template at /templates/admin/login.html
    }


    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        AdminUser admin = adminUserService.getCurrentAdminUser();
        if (admin == null) {
            return "redirect:/admin/login";
        }
        model.addAttribute("admin", admin);
        return "admin/dashboard";
    }


//    @GetMapping("/edit/{id}")
//    public String showEditForm(@PathVariable Long id, Model model) {
//        adminUserService.getById(id).ifPresent(admin -> model.addAttribute("admin", admin));
//        return "admin/edit";
//    }
//
//    public String updateAdmin(@ModelAttribute AdminUser adminUser) {
//        adminUserService.save(adminUser);
//        return "redirect:/admin/dashboard";
//    }

    @GetMapping("/delete/{id}")
    public String deleteAdmin(@PathVariable Long id) {
        adminUserService.delete(id);
        return "redirect:/admin/login";
    }
}