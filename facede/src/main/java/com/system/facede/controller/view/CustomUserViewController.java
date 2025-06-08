package com.system.facede.controller.view;

import com.system.facede.model.CustomUser;
import com.system.facede.service.CustomUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
    public String listUsers(Model model) {
        model.addAttribute("users", customUserService.getAll());
        return "users/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new CustomUser());
        return "users/create";
    }

    @PostMapping
    public String createUser(@ModelAttribute CustomUser user) {
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
    public String updateUser(@ModelAttribute("user") CustomUser user, BindingResult bindingResult) {
        customUserService.save(user);
        return "redirect:/users";
    }


    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        customUserService.delete(id);
        return "redirect:/users";
    }
}
