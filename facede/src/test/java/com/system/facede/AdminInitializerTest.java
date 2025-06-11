package com.system.facede;

import com.system.facede.model.AdminUser;
import com.system.facede.service.AdminUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.mockito.Mockito.*;

class AdminInitializerTest {

    @Mock
    private AdminUserService adminUserService;

    private AdminInitializer adminInitializer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adminInitializer = new AdminInitializer();
    }

    @Test
    void shouldCreateSuperAdminWhenNotExists() throws Exception {
        when(adminUserService.getByUsername("superadmin")).thenReturn(Optional.empty());

        adminInitializer.createSuperAdmin(adminUserService).run(new String[]{});

        verify(adminUserService).save(argThat(user ->
                user.getUsername().equals("superadmin") &&
                        user.getRole().equals("SUPER_ADMIN") &&
                        user.getPassword().equals("superpassword")
        ));
    }

    @Test
    void shouldNotCreateSuperAdminWhenAlreadyExists() throws Exception {
        AdminUser existingUser = new AdminUser();
        existingUser.setUsername("superadmin");

        when(adminUserService.getByUsername("superadmin")).thenReturn(Optional.of(existingUser));

        adminInitializer.createSuperAdmin(adminUserService).run(new String[]{});

        verify(adminUserService, never()).save(any());
    }
}
