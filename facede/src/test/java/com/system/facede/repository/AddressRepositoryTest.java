package com.system.facede.repository;

import com.system.facede.model.Address;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)  // use real PostgreSQL
@ActiveProfiles("test") // make sure you have application-test.yml/properties
class AddressRepositoryTest {

    @Autowired
    private AddressRepository addressRepository;

    @Test
    void findByCustomUserId_shouldReturnAddresses() {
        Address address = new Address();
        address.setId(123L);
        address.setType("EMAIL");
        address.setValue("test@domain.com");

        addressRepository.save(address); // ✅ ID will be auto-assigned

        List<Address> result = addressRepository.findByCustomUserId(123L);

        assertEquals(1, result.size());
        assertEquals("EMAIL", result.get(0).getType());
    }


    @Test
    void existsByTypeAndNormalizedValue_shouldReturnTrueIfExists() {
        Address address = new Address();
        address.setType("SMS");
        address.setValue("  555-1234 ");
        addressRepository.save(address);

        boolean exists = addressRepository.existsByTypeAndNormalizedValue("SMS", "555-1234");

        assertTrue(exists);
    }

    @Test
    void existsByTypeAndNormalizedValue_shouldReturnFalseIfNotExists() {
        boolean exists = addressRepository.existsByTypeAndNormalizedValue("EMAIL", "doesnotexist@example.com");
        assertFalse(exists);
    }

    @Test
    void findAllByTypeAndNormalizedValue_shouldReturnMatchingAddresses() {
        Address a1 = new Address();
        a1.setType("EMAIL");
        a1.setValue(" User@Example.com ");

        Address a2 = new Address();
        a2.setType("EMAIL");
        a2.setValue("user@example.com");

        addressRepository.saveAll(List.of(a1, a2));

        List<Address> result = addressRepository.findAllByTypeAndNormalizedValue("EMAIL", "user@example.com");

        assertEquals(2, result.size());
    }
}
