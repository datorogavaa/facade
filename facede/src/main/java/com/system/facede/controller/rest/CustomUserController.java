package com.system.facede.controller.rest;

import com.system.facede.dto.CustomUserBatchUpdateRequest;
import com.system.facede.model.CustomUser;
import com.system.facede.service.CustomUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        Optional<CustomUser> user = customUserService.getById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Custom user not found with ID: " + id);
        }
    }


    @PostMapping
    public CustomUser createUser(@RequestBody CustomUser customUser) {
        return customUserService.save(customUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody CustomUser updatedUser) {
        Optional<CustomUser> optionalUser = customUserService.getById(id);
        if (optionalUser.isPresent()) {
            CustomUser existingUser = optionalUser.get();
            existingUser.setName(updatedUser.getName());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
            customUserService.save(existingUser);
            return ResponseEntity.ok("User Updated Succesfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Custom user not found with ID: " + id);
        }
    }



    @PutMapping("/update-batch")
    public ResponseEntity<?> updateUser(@RequestBody List<CustomUserBatchUpdateRequest> request) {
        try {
            customUserService.updateUsersBatch(request);
            return ResponseEntity.ok("Batch update successful");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Batch update failed: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomUser(@PathVariable Long id) {
        Optional<CustomUser> optionalUser = customUserService.getById(id);
        if (optionalUser.isPresent()) {
            customUserService.delete(id);
            return ResponseEntity.ok("Deleted user successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Custom user not found with ID: " + id);
        }
    }

}
