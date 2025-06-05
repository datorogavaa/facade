package com.system.facede.controller.view;

import com.system.facede.model.CustomUser;
import com.system.facede.service.CustomUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        customUserService.getById(id).ifPresent(user -> model.addAttribute("user", user));
        return "users/edit";
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute CustomUser user) {
        customUserService.save(user);
        return "redirect:/users";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        customUserService.delete(id);
        return "redirect:/users";
    }
}
