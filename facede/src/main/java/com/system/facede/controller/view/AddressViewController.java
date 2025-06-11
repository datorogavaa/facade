package com.system.facede.controller.view;
import com.system.facede.model.Address;
import com.system.facede.service.AddressService;
import com.system.facede.service.CustomUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/addresses")
public class AddressViewController {

    private static final Logger logger = LoggerFactory.getLogger(AddressViewController.class);

    private final AddressService addressService;
    private final CustomUserService customUserService;

    public AddressViewController(AddressService addressService, CustomUserService customUserService) {
        this.addressService = addressService;
        this.customUserService = customUserService;
    }

    @GetMapping
    public String listAddresses(Model model) {
        logger.info("Listing all addresses");
        model.addAttribute("addresses", addressService.getAll());
        return "addresses/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        logger.info("Showing create address form");
        model.addAttribute("address", new Address());
        model.addAttribute("users", customUserService.getAll());
        return "addresses/create";
    }

    @PostMapping
    public String createAddress(@ModelAttribute Address address, Model model) {
        logger.info("Creating address for user ID: {}", address.getCustomUser() != null ? address.getCustomUser().getId() : null);
        address.setValue(address.getValue().trim().toLowerCase());

        if (addressService.addressExistsGlobally(address)) {
            logger.warn("Address type and value already used by another user: type={}, value={}", address.getType(), address.getValue());
            model.addAttribute("errorMessage", "This address type and value is already used by another user.");
            model.addAttribute("address", address);
            model.addAttribute("users", customUserService.getAll());
            return "addresses/create";
        }

        addressService.save(address);
        logger.info("Address created successfully");
        return "redirect:/addresses";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        logger.info("Showing edit form for address ID: {}", id);
        Optional<Address> addressOpt = addressService.getById(id);
        if (addressOpt.isEmpty()) {
            logger.warn("Address not found with ID: {}", id);
            model.addAttribute("errorMessage", "Address not found.");
            return "redirect:/addresses";
        }

        model.addAttribute("address", addressOpt.get());
        model.addAttribute("users", customUserService.getAll());
        return "addresses/edit";
    }

    @PostMapping("/update")
    public String updateAddress(@ModelAttribute Address address, Model model) {
        logger.info("Updating address ID: {}", address.getId());
        address.setValue(address.getValue().trim().toLowerCase());

        if (addressService.addressExistsGloballyForOther(address)) {
            logger.warn("Address type and value already used by another user (update): type={}, value={}", address.getType(), address.getValue());
            model.addAttribute("errorMessage", "This address type and value is already used by another user.");
            model.addAttribute("address", address);
            model.addAttribute("users", customUserService.getAll());
            return "addresses/edit";
        }

        addressService.save(address);
        logger.info("Address updated successfully");
        return "redirect:/addresses";
    }

    @GetMapping("/delete/{id}")
    public String deleteAddress(@PathVariable Long id) {
        logger.info("Deleting address with ID: {}", id);
        addressService.delete(id);
        logger.info("Address deleted successfully");
        return "redirect:/addresses";
    }
}