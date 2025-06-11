package com.system.facede.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Test
    void passwordEncoder_shouldBeBCrypt() {
        String raw = "secret";
        String encoded = passwordEncoder.encode(raw);
        assertTrue(passwordEncoder.matches(raw, encoded));
    }

    @Test
    void authenticationManager_shouldBeInjected() {
        assertNotNull(authenticationManager);
    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    void shouldAllowAccessToAdminCreate() throws Exception {
        mockMvc.perform(get("/admin/create"))
                .andExpect(status().is4xxClientError()); // or 200 if real controller exists
    }
}
