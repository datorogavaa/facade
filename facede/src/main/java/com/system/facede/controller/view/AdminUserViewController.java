package com.system.facede.controller.view;

import com.system.facede.dto.NotificationOptInReportDTO;
import com.system.facede.exception.PasswordEmptyException;
import com.system.facede.exception.UsernameAlreadyExistsException;
import com.system.facede.exception.UsernameEmptyException;
import com.system.facede.model.Address;
import com.system.facede.model.AdminUser;
import com.system.facede.model.CustomUser;
import com.system.facede.service.AddressService;
import com.system.facede.service.AdminUserService;
import com.system.facede.service.NotificationOptInReportingService;
import com.system.facede.service.CustomUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminUserViewController {

    private static final Logger logger = LoggerFactory.getLogger(AdminUserViewController.class);

    private final AdminUserService adminUserService;
    private final NotificationOptInReportingService reportService;
    private final CustomUserService customUserService;
    private final AddressService addressService;

    public AdminUserViewController(AdminUserService adminUserService,
                                   NotificationOptInReportingService reportService,
                                   CustomUserService customUserService, AddressService addressService) {
        this.adminUserService = adminUserService;
        this.reportService = reportService;
        this.customUserService = customUserService;
        this.addressService = addressService;
    }

    @GetMapping("/list")
    public String listAdmins(Model model) {
        logger.info("Listing all admin users");
        model.addAttribute("adminUsers", adminUserService.getAllAdmins());
        return "admin/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        logger.info("Showing create admin form");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("admin", new AdminUser());
        return "admin/create";
    }

    @PostMapping("/create")
    public String createAdmin(@ModelAttribute("admin") AdminUser adminUser, Model model) {
        logger.info("Creating new admin user with username: {}", adminUser.getUsername());
        try {
            adminUserService.save(adminUser);
            logger.info("Admin user created successfully");
            return "redirect:/admin/dashboard";
        } catch (UsernameAlreadyExistsException | PasswordEmptyException | UsernameEmptyException e) {
            logger.warn("Failed to create admin user: {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("admin", adminUser);
            return "admin/create";
        }
    }

    @GetMapping("/login")
    public String showLoginForm() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        logger.info("Showing admin login form");
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            logger.info("Already authenticated, redirecting to dashboard");
            return "redirect:/admin/dashboard";
        }
        return "admin/login";
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        logger.info("Showing admin dashboard");
        AdminUser admin = adminUserService.getCurrentAdminUser();
        if (admin == null) {
            logger.warn("No authenticated admin user, redirecting to login");
            return "redirect:/admin/login";
        }
        model.addAttribute("admin", admin);

        NotificationOptInReportDTO optInReport = reportService.getNotificationOptInReport();
        model.addAttribute("optInReport", optInReport);

        List<CustomUser> users = customUserService.getAll();
        model.addAttribute("users", users);

        List<Address> addresses = addressService.getAll();
        model.addAttribute("addresses", addresses);

        model.addAttribute("totalCustomers", users.size());
        model.addAttribute("totalEmails", optInReport.getEmailOptInCount());
        model.addAttribute("optedInSms", optInReport.getSmsOptInCount());
        model.addAttribute("optedInPostal", optInReport.getPostalOptInCount());

        return "admin/dashboard";
    }

    @GetMapping("/reset_password/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        logger.info("Showing reset password form for admin user ID: {}", id);
        adminUserService.getById(id).ifPresent(admin -> model.addAttribute("admin", admin));
        return "admin/reset_password";
    }

    @PostMapping("/update")
    public String updateAdmin(@ModelAttribute("admin") AdminUser admin,
                              @RequestParam("confirmPassword") String confirmPassword,
                              Model model) {
        logger.info("Updating admin user with ID: {}", admin.getId());
        if (!admin.getPassword().equals(confirmPassword)) {
            logger.warn("Password and confirm password do not match for admin user ID: {}", admin.getId());
            model.addAttribute("errorMessage", "Passwords do not match");
            return "admin/reset_password";
        }

        try {
            adminUserService.update(admin);
            logger.info("Admin user updated successfully");
            return "redirect:/admin/dashboard";
        } catch (Exception e) {
            logger.error("Failed to update admin user: {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/reset_password";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteAdmin(@PathVariable Long id) {
        logger.info("Deleting admin user with ID: {}", id);
        adminUserService.delete(id);
        logger.info("Admin user deleted successfully");
        return "redirect:/admin/dashboard";
    }
}