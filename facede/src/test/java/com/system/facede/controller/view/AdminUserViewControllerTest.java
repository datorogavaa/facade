package com.system.facede.controller.view;

import com.system.facede.dto.NotificationOptInReportDTO;
import com.system.facede.exception.PasswordEmptyException;
import com.system.facede.model.Address;
import com.system.facede.model.AdminUser;
import com.system.facede.model.CustomUser;
import com.system.facede.service.AddressService;
import com.system.facede.service.AdminUserService;
import com.system.facede.service.CustomUserService;
import com.system.facede.service.NotificationOptInReportingService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminUserViewController.class)
@Import(AdminUserViewControllerTest.Config.class)
class AdminUserViewControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private AdminUserService adminUserService;
    @Autowired private NotificationOptInReportingService reportService;
    @Autowired private CustomUserService customUserService;
    @Autowired private AddressService addressService;

    @TestConfiguration
    static class Config {
        @Bean public AdminUserService adminUserService() { return Mockito.mock(AdminUserService.class); }
        @Bean public NotificationOptInReportingService reportService() { return Mockito.mock(NotificationOptInReportingService.class); }
        @Bean public CustomUserService customUserService() { return Mockito.mock(CustomUserService.class); }
        @Bean public AddressService addressService() { return Mockito.mock(AddressService.class); }
    }

    @Test
    void listAdmins_shouldRenderListPage() throws Exception {
        when(adminUserService.getAllAdmins()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/admin/list")
                        .with(user("superadmin").roles("SUPER_ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/list"))
                .andExpect(model().attributeExists("adminUsers"));
    }

    @Test
    void showCreateForm_shouldRenderForm() throws Exception {
        mockMvc.perform(get("/admin/new")
                        .with(user("superadmin").roles("SUPER_ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/create"))
                .andExpect(model().attributeExists("admin"));
    }


    @Test
    void createAdmin_shouldRedirectWithFlashAttrBinding() throws Exception {
        AdminUser adminUser = new AdminUser();
        adminUser.setUsername("admin");
        adminUser.setPassword("pass123");

        when(adminUserService.save(any(AdminUser.class))).thenAnswer(invocation -> {
            AdminUser user = invocation.getArgument(0);
            System.out.println("Captured in mock: " + user.getUsername() + ", " + user.getPassword());
            user.setId(1L);
            return user;
        });

        mockMvc.perform(post("/admin/create")
                        .with(user("superadmin").roles("SUPER_ADMIN"))
                        .with(csrf())
                        .flashAttr("admin", adminUser)) // ✅ Must match th:object="${admin}"
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/dashboard"));

        ArgumentCaptor<AdminUser> captor = ArgumentCaptor.forClass(AdminUser.class);
        verify(adminUserService).save(captor.capture());

        AdminUser saved = captor.getValue();
        assertEquals("admin", saved.getUsername());
        assertEquals("pass123", saved.getPassword());
    }







    @Test
    void createAdmin_shouldReturnCreateOnError() throws Exception {
        doThrow(new PasswordEmptyException("Password must not be empty."))
                .when(adminUserService).save(any());

        mockMvc.perform(post("/admin/create")
                        .with(user("superadmin").roles("SUPER_ADMIN"))
                        .with(csrf())
                        .param("username", "admin")
                        .param("password", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/create"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attributeExists("admin"));
    }

    @Test
    void showDashboard_shouldDisplayData() throws Exception {
        AdminUser admin = new AdminUser();
        NotificationOptInReportDTO report = new NotificationOptInReportDTO(1L, 2L, 3L);

        when(adminUserService.getCurrentAdminUser()).thenReturn(admin);
        when(reportService.getNotificationOptInReport()).thenReturn(report);
        when(customUserService.getAll()).thenReturn(Collections.singletonList(new CustomUser()));
        when(addressService.getAll()).thenReturn(Collections.singletonList(new Address()));

        mockMvc.perform(get("/admin/dashboard")
                        .with(user("superadmin").roles("SUPER_ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/dashboard"))
                .andExpect(model().attributeExists("admin"))
                .andExpect(model().attributeExists("optInReport"))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attributeExists("addresses"));
    }

    @Test
    void updateAdmin_shouldMatchPasswordAndRedirect() throws Exception {
        AdminUser admin = new AdminUser();
        admin.setId(1L);
        admin.setPassword("123456");

        mockMvc.perform(post("/admin/update")
                        .with(user("superadmin").roles("SUPER_ADMIN"))
                        .with(csrf())
                        .param("password", "123456")
                        .param("confirmPassword", "123456")
                        .flashAttr("admin", admin))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/dashboard"));
    }

    @Test
    void updateAdmin_shouldReturnFormOnMismatch() throws Exception {
        AdminUser admin = new AdminUser();
        admin.setId(1L);
        admin.setPassword("123456");

        mockMvc.perform(post("/admin/update")
                        .with(user("superadmin").roles("SUPER_ADMIN"))
                        .with(csrf())
                        .param("password", "123456")
                        .param("confirmPassword", "654321")
                        .flashAttr("admin", admin))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/reset_password"))
                .andExpect(model().attributeExists("errorMessage"));
    }

    @Test
    void deleteAdmin_shouldRedirect() throws Exception {
        mockMvc.perform(get("/admin/delete/1")
                        .with(user("superadmin").roles("SUPER_ADMIN")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/dashboard"));
    }
}
