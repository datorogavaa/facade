package com.system.facede.controller.rest;

import com.system.facede.model.Address;
import com.system.facede.service.AddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private static final Logger logger = LoggerFactory.getLogger(AddressController.class);

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping
    public List<Address> getAllAddresses() {
        logger.info("Fetching all addresses");
        return addressService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAddress(@PathVariable Long id) {
        logger.info("Fetching address with ID: {}", id);
        Optional<Address> address = addressService.getById(id);
        if (address.isPresent()) {
            return ResponseEntity.ok(address.get());
        } else {
            logger.warn("Address not found with ID: {}", id);
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Address with that id does not exist");
        }
    }

    @PostMapping
    public Address createAddress(@RequestBody Address address) {
        logger.info("Creating new address");
        return addressService.save(address);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAddress(@PathVariable Long id, @RequestBody Address updatedAddress) {
        logger.info("Updating address with ID: {}", id);
        Optional<Address> optionalAddress = addressService.getById(id);
        if (optionalAddress.isPresent()) {
            Address existing = optionalAddress.get();
            existing.setType(updatedAddress.getType());
            existing.setValue(updatedAddress.getValue());
            existing.setCustomUser(updatedAddress.getCustomUser());
            return ResponseEntity.ok(addressService.save(existing));
        } else {
            logger.warn("Address not found for update with ID: {}", id);
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Address not found with ID: " + id);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAddress(@PathVariable Long id) {
        logger.info("Deleting address with ID: {}", id);
        Optional<Address> optionalAddress = addressService.getById(id);
        if (optionalAddress.isPresent()) {
            addressService.delete(id);
            return ResponseEntity.ok("Deleted address successfully");
        } else {
            logger.warn("Address not found for deletion with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Address not found with ID: " + id);
        }
    }

}