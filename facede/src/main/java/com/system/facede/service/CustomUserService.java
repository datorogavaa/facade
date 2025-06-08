package com.system.facede.service;

import com.system.facede.model.CustomUser;
import com.system.facede.repository.CustomUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CustomUserService {
    private final CustomUserRepository customUserRepository;

    public CustomUserService(CustomUserRepository customUserRepository) {
        this.customUserRepository = customUserRepository;
    }

    public List<CustomUser> getAll() {
        return customUserRepository.findAll();
    }

    public Optional<CustomUser> getById(Long id) {
        return customUserRepository.findById(id);
    }

    public CustomUser save(CustomUser user) {
        return customUserRepository.save(user);
    }

//    @Transactional
//    public CustomUser update(Long id, CustomUser updatedUser) {
//        CustomUser existingUser = customUserRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("User not found with id " + id));
//
//        System.out.println("Updating user ID: " + id);
//        System.out.println("Old name: " + existingUser.getName() + ", New name: " + updatedUser.getName());
//
//        existingUser.setName(updatedUser.getName());
//        existingUser.setEmail(updatedUser.getEmail());
//        existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
//
//        return existingUser;
//    }



    public void delete(Long id) {
        customUserRepository.deleteById(id);
    }
}

