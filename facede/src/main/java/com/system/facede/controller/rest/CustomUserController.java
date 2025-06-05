package com.system.facede.controller.rest;

import com.system.facede.model.CustomUser;
import com.system.facede.service.CustomUserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/custom-users")
public class CustomUserController {

    private final CustomUserService customUserService;

    public CustomUserController(CustomUserService customUserService) {
        this.customUserService = customUserService;
    }

    @GetMapping
    public List<CustomUser> getAllUsers() {
        return customUserService.getAll();
    }

    @PostMapping
    public CustomUser createUser(@RequestBody CustomUser customUser) {
        return customUserService.save(customUser);
    }

    @PutMapping("/{id}")
    public CustomUser updateUser(@PathVariable Long id, @RequestBody CustomUser updatedUser) {
        Optional<CustomUser> optionalUser = customUserService.getById(id);
        if (optionalUser.isPresent()) {
            CustomUser existingUser = optionalUser.get();
            existingUser.setName(updatedUser.getName());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
            existingUser.setNotificationPreference(updatedUser.getNotificationPreference());
            return customUserService.save(existingUser);
        } else {
            throw new RuntimeException("User not found with ID: " + id);
        }
    }


    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        customUserService.delete(id);
    }
}
