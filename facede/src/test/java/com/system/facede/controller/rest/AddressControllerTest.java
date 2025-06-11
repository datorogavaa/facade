package com.system.facede.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.system.facede.model.Address;
import com.system.facede.service.AddressService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AddressController.class)
@Import(AddressControllerTest.TestConfig.class)
class AddressControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private AddressService addressService;
    @Autowired private ObjectMapper objectMapper;

    @TestConfiguration
    static class TestConfig {
        @Bean public AddressService addressService() {
            return Mockito.mock(AddressService.class);
        }
    }

    @Test
    void getAllAddresses_shouldReturnList() throws Exception {
        when(addressService.getAll()).thenReturn(Collections.singletonList(new Address()));

        mockMvc.perform(get("/api/addresses")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getAddress_shouldReturnAddressIfFound() throws Exception {
        Address address = new Address();
        address.setId(1L);
        address.setValue("Test Value");

        when(addressService.getById(1L)).thenReturn(Optional.of(address));

        mockMvc.perform(get("/api/addresses/1")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.value").value("Test Value"));
    }

    @Test
    void getAddress_shouldReturn404IfNotFound() throws Exception {
        when(addressService.getById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/addresses/1")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Address with that id does not exist"));
    }

    @Test
    void createAddress_shouldReturnSavedAddress() throws Exception {
        Address address = new Address();
        address.setType("Email");
        address.setValue("example@test.com");

        Address saved = new Address();
        saved.setId(1L);
        saved.setType("Email");
        saved.setValue("example@test.com");

        when(addressService.save(any(Address.class))).thenReturn(saved);

        mockMvc.perform(post("/api/addresses")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(address)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.value").value("example@test.com"));
    }

    @Test
    void updateAddress_shouldReturnUpdatedIfFound() throws Exception {
        Address existing = new Address();
        existing.setId(1L);
        existing.setType("SMS");
        existing.setValue("old@example.com");

        Address update = new Address();
        update.setType("Email");
        update.setValue("new@example.com");

        when(addressService.getById(1L)).thenReturn(Optional.of(existing));
        when(addressService.save(any(Address.class))).thenAnswer(inv -> inv.getArgument(0));

        mockMvc.perform(put("/api/addresses/1")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("Email"))
                .andExpect(jsonPath("$.value").value("new@example.com"));
    }

    @Test
    void updateAddress_shouldReturn404IfNotFound() throws Exception {
        when(addressService.getById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/addresses/1")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new Address())))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Address not found with ID: 1"));
    }

    @Test
    void deleteAddress_shouldReturnSuccessIfFound() throws Exception {
        when(addressService.getById(1L)).thenReturn(Optional.of(new Address()));

        mockMvc.perform(delete("/api/addresses/1")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted address successfully"));
    }

    @Test
    void deleteAddress_shouldReturn404IfNotFound() throws Exception {
        when(addressService.getById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/addresses/1")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Address not found with ID: 1"));
    }
}
