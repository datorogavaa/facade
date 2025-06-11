package com.system.facede.service;

import com.system.facede.model.Address;
import com.system.facede.repository.AddressRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService {

    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public List<Address> getAll() {
        return addressRepository.findAll();
    }

    public Optional<Address> getById(Long id) {
        return addressRepository.findById(id);
    }

    public List<Address> getByCustomerUserId(Long customerUserId) {
        return addressRepository.findByCustomUserId(customerUserId);
    }

    public Address save(Address address) {
        // Normalize before saving
        if (address.getValue() != null) {
            address.setValue(address.getValue().trim().toLowerCase());
        }
        return addressRepository.save(address);
    }

    // Check for global existence (for creation)
    public boolean addressExistsGlobally(Address address) {
        return addressRepository.existsByTypeAndNormalizedValue(
                address.getType(),
                address.getValue().trim()
        );
    }

    // Check for global duplicates excluding current address (for update)
    public boolean addressExistsGloballyForOther(Address address) {
        List<Address> matches = addressRepository.findAllByTypeAndNormalizedValue(
                address.getType(),
                address.getValue().trim()
        );
        return matches.stream()
                .anyMatch(existing -> !existing.getId().equals(address.getId()));
    }

    public void delete(Long id) {
        addressRepository.deleteById(id);
    }
}

