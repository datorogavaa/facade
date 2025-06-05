package com.system.facede.service;

import com.system.facede.model.AdminUser;
import com.system.facede.repository.AdminUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminUserService {
    private final AdminUserRepository adminUserRepository;

    public AdminUserService(AdminUserRepository adminUserRepository) {
        this.adminUserRepository = adminUserRepository;
    }

    public List<AdminUser> getAllAdmins() {
        return adminUserRepository.findAll();
    }

    public Optional<AdminUser> getById(Long id) {
        return adminUserRepository.findById(id);
    }

    public Optional<AdminUser> getByUsername(String username) {
        return adminUserRepository.findByUsername(username);
    }

    public AdminUser save(AdminUser adminUser) {
        return adminUserRepository.save(adminUser);
    }

    public void delete(Long id) {
        adminUserRepository.deleteById(id);
    }
}
