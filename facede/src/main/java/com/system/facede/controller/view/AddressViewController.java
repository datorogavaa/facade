package com.system.facede.controller.view;

import com.system.facede.model.Address;
import com.system.facede.service.AddressService;
import com.system.facede.service.CustomUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/addresses")
public class AddressViewController {

    private final AddressService addressService;
    private final CustomUserService customUserService;

    public AddressViewController(AddressService addressService, CustomUserService customUserService) {
        this.addressService = addressService;
        this.customUserService = customUserService;
    }

    @GetMapping
    public String listAddresses(Model model) {
        model.addAttribute("addresses", addressService.getAll());
        return "addresses/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("address", new Address());
        model.addAttribute("users", customUserService.getAll());
        return "addresses/create";
    }

    @PostMapping
    public String createAddress(@ModelAttribute Address address, Model model) {
        address.setValue(address.getValue().trim().toLowerCase());

        if (addressService.addressExistsGlobally(address)) {
            model.addAttribute("errorMessage", "This address type and value is already used by another user.");
            model.addAttribute("address", address);
            model.addAttribute("users", customUserService.getAll());
            return "addresses/create";
        }

        addressService.save(address);
        return "redirect:/addresses";
    }


    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Address> addressOpt = addressService.getById(id);
        if (addressOpt.isEmpty()) {
            model.addAttribute("errorMessage", "Address not found.");
            return "redirect:/addresses";
        }

        model.addAttribute("address", addressOpt.get());
        model.addAttribute("users", customUserService.getAll());
        return "addresses/edit";
    }

    @PostMapping("/update")
    public String updateAddress(@ModelAttribute Address address, Model model) {
        address.setValue(address.getValue().trim().toLowerCase());

        if (addressService.addressExistsGloballyForOther(address)) {
            model.addAttribute("errorMessage", "This address type and value is already used by another user.");
            model.addAttribute("address", address);
            model.addAttribute("users", customUserService.getAll());
            return "addresses/edit";
        }

        addressService.save(address);
        return "redirect:/addresses";
    }


    @GetMapping("/delete/{id}")
    public String deleteAddress(@PathVariable Long id) {
        addressService.delete(id);
        return "redirect:/addresses";
    }
}
