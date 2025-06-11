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
        model.addAttribute("adminUsers", adminUserService.getAllAdmins());
        return "admin/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

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
            model.addAttribute("admin", adminUser);
            return "admin/create";
        } catch (PasswordEmptyException e) {
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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            return "redirect:/admin/dashboard";
        }
        return "admin/login";
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        AdminUser admin = adminUserService.getCurrentAdminUser();
        if (admin == null) {
            return "redirect:/admin/login";
        }
        model.addAttribute("admin", admin);

        NotificationOptInReportDTO optInReport = reportService.getNotificationOptInReport();
        model.addAttribute("optInReport", optInReport);

        List<CustomUser> users = customUserService.getAll();
        model.addAttribute("users", users);

        // Add addresses
        List<Address> addresses = addressService.getAll();
        model.addAttribute("addresses", addresses);


        // Add summary card values
        model.addAttribute("totalCustomers", users.size());
        model.addAttribute("totalEmails", optInReport.getEmailOptInCount());
        model.addAttribute("optedInSms", optInReport.getSmsOptInCount());
        model.addAttribute("optedInPostal", optInReport.getPostalOptInCount());

        return "admin/dashboard";
    }

    @GetMapping("/reset_password/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        adminUserService.getById(id).ifPresent(admin -> model.addAttribute("admin", admin));
        return "admin/reset_password";
    }

    @PostMapping("/update")
    public String updateAdmin(@ModelAttribute("admin") AdminUser admin,
                              @RequestParam("confirmPassword") String confirmPassword,
                              Model model) {
        if (!admin.getPassword().equals(confirmPassword)) {
            model.addAttribute("errorMessage", "Passwords do not match");
            return "admin/reset_password";
        }

        try {
            adminUserService.update(admin);
            return "redirect:/admin/dashboard";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/reset_password";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteAdmin(@PathVariable Long id) {
        adminUserService.delete(id);
        return "redirect:/admin/dashboard";
    }
}