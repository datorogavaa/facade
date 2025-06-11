package com.system.facede.service;

import com.system.facede.model.Address;
import com.system.facede.repository.AddressRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService {

    private static final Logger logger = LoggerFactory.getLogger(AddressService.class);

    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public List<Address> getAll() {
        logger.info("Fetching all addresses");
        return addressRepository.findAll();
    }

    public Optional<Address> getById(Long id) {
        logger.info("Fetching address by ID: {}", id);
        return addressRepository.findById(id);
    }

    public List<Address> getByCustomerUserId(Long customerUserId) {
        logger.info("Fetching addresses for customer user ID: {}", customerUserId);
        return addressRepository.findByCustomUserId(customerUserId);
    }

    public Address save(Address address) {
        logger.info("Saving address: {}", address);
        if (address.getValue() != null) {
            address.setValue(address.getValue().trim().toLowerCase());
        }
        Address saved = addressRepository.save(address);
        logger.info("Address saved with ID: {}", saved.getId());
        return saved;
    }

    public boolean addressExistsGlobally(Address address) {
        logger.info("Checking if address exists globally: {}", address);
        boolean exists = addressRepository.existsByTypeAndNormalizedValue(
                address.getType(),
                address.getValue().trim()
        );
        logger.info("Address exists globally: {}", exists);
        return exists;
    }

    public boolean addressExistsGloballyForOther(Address address) {
        logger.info("Checking if address exists globally for other: {}", address);
        List<Address> matches = addressRepository.findAllByTypeAndNormalizedValue(
                address.getType(),
                address.getValue().trim()
        );
        boolean existsForOther = matches.stream()
                .anyMatch(existing -> !existing.getId().equals(address.getId()));
        logger.info("Address exists globally for other: {}", existsForOther);
        return existsForOther;
    }

    public void delete(Long id) {
        logger.info("Deleting address with ID: {}", id);
        addressRepository.deleteById(id);
        logger.info("Address deleted with ID: {}", id);
    }
}