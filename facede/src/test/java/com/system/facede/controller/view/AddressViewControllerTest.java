package com.system.facede.controller.view;

import com.system.facede.model.Address;
import com.system.facede.model.CustomUser;
import com.system.facede.service.AddressService;
import com.system.facede.service.CustomUserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AddressViewController.class)
@Import(AddressViewControllerTest.Config.class)
class AddressViewControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private AddressService addressService;
    @Autowired private CustomUserService customUserService;

    @TestConfiguration
    static class Config {
        @Bean public AddressService addressService() { return Mockito.mock(AddressService.class); }
        @Bean public CustomUserService customUserService() { return Mockito.mock(CustomUserService.class); }
    }

    @Test
    void listAddresses_shouldReturnListView() throws Exception {
        when(addressService.getAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/addresses")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("addresses/list"))
                .andExpect(model().attributeExists("addresses"));
    }

    @Test
    void showCreateForm_shouldReturnCreateView() throws Exception {
        when(customUserService.getAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/addresses/new")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("addresses/create"))
                .andExpect(model().attributeExists("address"))
                .andExpect(model().attributeExists("users"));
    }

    @Test
    void createAddress_shouldRedirectOnSuccess() throws Exception {
        Address address = new Address();
        when(addressService.addressExistsGlobally(any(Address.class))).thenReturn(false);

        mockMvc.perform(post("/addresses")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf())
                        .param("type", "Email")
                        .param("value", "test@example.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/addresses"));
    }

    @Test
    void createAddress_shouldReturnCreateViewOnDuplicate() throws Exception {
        when(addressService.addressExistsGlobally(any(Address.class))).thenReturn(true);
        when(customUserService.getAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(post("/addresses")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf())
                        .param("type", "Email")
                        .param("value", "test@example.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("addresses/create"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attributeExists("address"))
                .andExpect(model().attributeExists("users"));
    }

    @Test
    void showEditForm_shouldReturnEditView() throws Exception {
        Address address = new Address();
        when(addressService.getById(1L)).thenReturn(Optional.of(address));
        when(customUserService.getAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/addresses/edit/1")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("addresses/edit"))
                .andExpect(model().attributeExists("address"))
                .andExpect(model().attributeExists("users"));
    }

    @Test
    void showEditForm_shouldRedirectIfNotFound() throws Exception {
        when(addressService.getById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/addresses/edit/1")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/addresses"));
    }

    @Test
    void updateAddress_shouldRedirectOnSuccess() throws Exception {
        when(addressService.addressExistsGloballyForOther(any(Address.class))).thenReturn(false);

        mockMvc.perform(post("/addresses/update")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf())
                        .param("type", "Email")
                        .param("value", "updated@example.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/addresses"));
    }

    @Test
    void updateAddress_shouldReturnEditViewOnDuplicate() throws Exception {
        when(addressService.addressExistsGloballyForOther(any(Address.class))).thenReturn(true);
        when(customUserService.getAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(post("/addresses/update")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf())
                        .param("type", "Email")
                        .param("value", "duplicate@example.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("addresses/edit"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attributeExists("address"))
                .andExpect(model().attributeExists("users"));
    }

    @Test
    void deleteAddress_shouldRedirect() throws Exception {
        mockMvc.perform(get("/addresses/delete/1")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/addresses"));
    }
}
