package com.system.facede.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomUserBatchUpdateRequestTest {

    private CustomUserBatchUpdateRequest request;

    @BeforeEach
    void setUp() {
        request = new CustomUserBatchUpdateRequest();
    }

    @Test
    void testUserId() {
        Long userId = 123L;
        request.setUserId(userId);
        assertEquals(userId, request.getUserId());
    }

    @Test
    void testName() {
        String name = "John Doe";
        request.setName(name);
        assertEquals(name, request.getName());
    }

    @Test
    void testEmail() {
        String email = "john@example.com";
        request.setEmail(email);
        assertEquals(email, request.getEmail());
    }

    @Test
    void testPhoneNumber() {
        String phoneNumber = "+1234567890";
        request.setPhoneNumber(phoneNumber);
        assertEquals(phoneNumber, request.getPhoneNumber());
    }
}
