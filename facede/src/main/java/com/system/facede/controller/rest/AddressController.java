package com.system.facede.controller.rest;

import com.system.facede.model.Address;
import com.system.facede.service.AddressService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping
    public List<Address> getAllAddresses() {
        return addressService.getAll();
    }

    @PostMapping
    public Address createAddress(@RequestBody Address address) {
        return addressService.save(address);
    }

    @PutMapping("/{id}")
    public Address updateAddress(@PathVariable Long id, @RequestBody Address updatedAddress) {
        Optional<Address> optionalAddress = addressService.getById(id);
        if (optionalAddress.isPresent()) {
            Address existing = optionalAddress.get();
            existing.setType(updatedAddress.getType());
            existing.setValue(updatedAddress.getValue());
            existing.setCustomUser(updatedAddress.getCustomUser());
            return addressService.save(existing);
        } else {
            throw new RuntimeException("Address not found with ID: " + id);
        }
    }


    @DeleteMapping("/{id}")
    public void deleteAddress(@PathVariable Long id) {
        addressService.delete(id);
    }
}
