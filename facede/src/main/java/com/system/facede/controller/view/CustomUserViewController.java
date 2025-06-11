package com.system.facede.controller.view;

import com.system.facede.model.CustomUser;
import com.system.facede.service.CustomUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@PreAuthorize("hasRole('ADMIN')")
@Controller
@RequestMapping("/users")
public class CustomUserViewController {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserViewController.class);

    private final CustomUserService customUserService;

    public CustomUserViewController(CustomUserService customUserService) {
        this.customUserService = customUserService;
    }

    @GetMapping
    public String listUsers(@RequestParam(required = false) String name,
                            @RequestParam(required = false) String email,
                            @RequestParam(required = false) String phone,
                            @RequestParam(defaultValue = "name") String sortField,
                            @RequestParam(defaultValue = "asc") String sortDir,
                            Model model) {

        logger.info("Listing users with filters - name: {}, email: {}, phone: {}, sortField: {}, sortDir: {}", name, email, phone, sortField, sortDir);
        Sort sort = sortDir.equals("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        List<CustomUser> users = customUserService.getFilteredUsers(name, email, phone, sort);

        model.addAttribute("users", users);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        return "users/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        logger.info("Showing create user form");
        model.addAttribute("user", new CustomUser());
        return "users/create";
    }

    @PostMapping
    public String createUser(@ModelAttribute CustomUser user, Model model) {
        logger.info("Creating user with name: {}, email: {}, phone: {}", user.getName(), user.getEmail(), user.getPhoneNumber());
        if (user.getEmail() != null && customUserService.existsByEmail(user.getEmail())) {
            logger.warn("User creation failed: email already exists ({})", user.getEmail());
            model.addAttribute("errorMessage", "A user with this email already exists.");
            model.addAttribute("user", user);
            return "users/create";
        }

        if (user.getName() != null && customUserService.existsByName(user.getName())) {
            logger.warn("User creation failed: username already exists ({})", user.getName());
            model.addAttribute("errorMessage", "A user with this username already exists.");
            model.addAttribute("user", user);
            return "users/create";
        }

        if (user.getPhoneNumber() != null && customUserService.existsByPhoneNumber(user.getPhoneNumber())) {
            logger.warn("User creation failed: phone number already exists ({})", user.getPhoneNumber());
            model.addAttribute("errorMessage", "A user with this phone number already exists.");
            model.addAttribute("user", user);
            return "users/create";
        }

        customUserService.save(user);
        logger.info("User created successfully");
        return "redirect:/users";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        logger.info("Showing edit form for user ID: {}", id);
        Optional<CustomUser> userOpt = customUserService.getById(id);
        if (userOpt.isPresent()) {
            CustomUser user = userOpt.get();
            logger.info("Editing user: {} (name: {})", user.getId(), user.getName());
            model.addAttribute("user", user);
        } else {
            logger.warn("User not found for ID: {}", id);
        }
        return "users/edit";
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute("user") CustomUser user, Model model) {
        Long currentUserId = user.getId();
        logger.info("Updating user with ID: {}", currentUserId);

        Optional<CustomUser> existingEmail = customUserService.findByEmail(user.getEmail());
        if (existingEmail.isPresent() && !existingEmail.get().getId().equals(currentUserId)) {
            logger.warn("Update failed: email already exists ({})", user.getEmail());
            model.addAttribute("errorMessage", "A user with this email already exists.");
            model.addAttribute("user", user);
            return "users/edit";
        }

        Optional<CustomUser> existingName = customUserService.findByName(user.getName());
        if (existingName.isPresent() && !existingName.get().getId().equals(currentUserId)) {
            logger.warn("Update failed: username already exists ({})", user.getName());
            model.addAttribute("errorMessage", "A user with this username already exists.");
            model.addAttribute("user", user);
            return "users/edit";
        }

        Optional<CustomUser> existingPhone = customUserService.findByPhoneNumber(user.getPhoneNumber());
        if (existingPhone.isPresent() && !existingPhone.get().getId().equals(currentUserId)) {
            logger.warn("Update failed: phone number already exists ({})", user.getPhoneNumber());
            model.addAttribute("errorMessage", "A user with this phone number already exists.");
            model.addAttribute("user", user);
            return "users/edit";
        }

        customUserService.save(user);
        logger.info("User updated successfully");
        return "redirect:/users";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        logger.info("Deleting user with ID: {}", id);
        customUserService.delete(id);
        logger.info("User deleted successfully");
        return "redirect:/users";
    }
}