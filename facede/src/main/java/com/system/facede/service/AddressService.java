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
        return addressRepository.save(address);
    }

    public void delete(Long id) {
        addressRepository.deleteById(id);
    }
}
