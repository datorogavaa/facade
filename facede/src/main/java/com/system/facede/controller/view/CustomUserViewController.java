package com.system.facede.controller.view;

import com.system.facede.model.CustomUser;
import com.system.facede.service.CustomUserService;
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
        model.addAttribute("user", new CustomUser());
        return "users/create";
    }

    @PostMapping
    public String createUser(@ModelAttribute CustomUser user, Model model) {
        if (user.getEmail() != null && customUserService.existsByEmail(user.getEmail())) {
            model.addAttribute("errorMessage", "A user with this email already exists.");
            model.addAttribute("user", user);
            return "users/create";
        }

        if (user.getName() != null && customUserService.existsByName(user.getName())) {
            model.addAttribute("errorMessage", "A user with this username already exists.");
            model.addAttribute("user", user);
            return "users/create";
        }

        if (user.getPhoneNumber() != null && customUserService.existsByPhoneNumber(user.getPhoneNumber())) {
            model.addAttribute("errorMessage", "A user with this phone number already exists.");
            model.addAttribute("user", user);
            return "users/create";
        }

        customUserService.save(user);
        return "redirect:/users";
    }



    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<CustomUser> userOpt = customUserService.getById(id);
        if (userOpt.isPresent()) {
            CustomUser user = userOpt.get();
            System.out.println("Editing user: " + user.getId() + ", name: " + user.getName());
            model.addAttribute("user", user);
        } else {
            System.out.println("User not found for ID: " + id);
        }
        return "users/edit";
    }


    @PostMapping("/update")
    public String updateUser(@ModelAttribute("user") CustomUser user, Model model) {
        Long currentUserId = user.getId();

        Optional<CustomUser> existingEmail = customUserService.findByEmail(user.getEmail());
        if (existingEmail.isPresent() && !existingEmail.get().getId().equals(currentUserId)) {
            model.addAttribute("errorMessage", "A user with this email already exists.");
            model.addAttribute("user", user);
            return "users/edit";
        }

        Optional<CustomUser> existingName = customUserService.findByName(user.getName());
        if (existingName.isPresent() && !existingName.get().getId().equals(currentUserId)) {
            model.addAttribute("errorMessage", "A user with this username already exists.");
            model.addAttribute("user", user);
            return "users/edit";
        }

        Optional<CustomUser> existingPhone = customUserService.findByPhoneNumber(user.getPhoneNumber());
        if (existingPhone.isPresent() && !existingPhone.get().getId().equals(currentUserId)) {
            model.addAttribute("errorMessage", "A user with this phone number already exists.");
            model.addAttribute("user", user);
            return "users/edit";
        }

        customUserService.save(user);
        return "redirect:/users";
    }


    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        customUserService.delete(id);
        return "redirect:/users";
    }
}
