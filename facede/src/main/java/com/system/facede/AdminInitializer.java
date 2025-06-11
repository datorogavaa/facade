package com.system.facede;

import com.system.facede.model.AdminUser;
import com.system.facede.service.AdminUserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdminInitializer {

    @Bean
    public CommandLineRunner createSuperAdmin(AdminUserService adminUserService) {
        return args -> {
            if (adminUserService.getByUsername("superadmin").isEmpty()) {
                AdminUser superAdmin = new AdminUser();
                superAdmin.setUsername("superadmin");
                superAdmin.setPassword("superpassword");
                superAdmin.setRole("ROLE_SUPER_ADMIN");
                adminUserService.save(superAdmin);
            }
        };
    }
}