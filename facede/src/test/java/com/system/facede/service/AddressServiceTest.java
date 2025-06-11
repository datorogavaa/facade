package com.system.facede.service;

import com.system.facede.model.Address;
import com.system.facede.repository.AddressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddressServiceTest {

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressService addressService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAll_shouldReturnAllAddresses() {
        List<Address> addresses = List.of(new Address(), new Address());
        when(addressRepository.findAll()).thenReturn(addresses);

        List<Address> result = addressService.getAll();

        assertEquals(2, result.size());
        verify(addressRepository).findAll();
    }

    @Test
    void getById_shouldReturnAddressIfExists() {
        Address address = new Address();
        address.setId(1L);
        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));

        Optional<Address> result = addressService.getById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void getByCustomerUserId_shouldReturnAddresses() {
        List<Address> addresses = List.of(new Address());
        when(addressRepository.findByCustomUserId(123L)).thenReturn(addresses);

        List<Address> result = addressService.getByCustomerUserId(123L);

        assertEquals(1, result.size());
    }

    @Test
    void save_shouldNormalizeAndSaveAddress() {
        Address address = new Address();
        address.setValue("  Example@TEST.Com ");
        address.setType("EMAIL");

        Address saved = new Address();
        saved.setId(1L);
        saved.setValue("example@test.com");
        saved.setType("EMAIL");

        when(addressRepository.save(any(Address.class))).thenReturn(saved);

        Address result = addressService.save(address);

        assertEquals("example@test.com", result.getValue());
        verify(addressRepository).save(any(Address.class));
    }

    @Test
    void addressExistsGlobally_shouldReturnTrueIfExists() {
        Address address = new Address();
        address.setType("SMS");
        address.setValue("value ");

        when(addressRepository.existsByTypeAndNormalizedValue("SMS", "value")).thenReturn(true);

        boolean exists = addressService.addressExistsGlobally(address);

        assertTrue(exists);
    }

    @Test
    void addressExistsGloballyForOther_shouldDetectDuplicateExcludingItself() {
        Address address = new Address();
        address.setId(2L);
        address.setType("EMAIL");
        address.setValue("abc@example.com");

        Address other = new Address();
        other.setId(1L);
        other.setValue("abc@example.com");
        other.setType("EMAIL");

        when(addressRepository.findAllByTypeAndNormalizedValue("EMAIL", "abc@example.com"))
                .thenReturn(List.of(other));

        boolean result = addressService.addressExistsGloballyForOther(address);

        assertTrue(result);
    }

    @Test
    void delete_shouldCallRepositoryDeleteById() {
        addressService.delete(123L);
        verify(addressRepository).deleteById(123L);
    }
}
