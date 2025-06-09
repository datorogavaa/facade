package com.system.facede.controller.view;

import com.system.facede.model.Address;
import com.system.facede.service.AddressService;
import com.system.facede.service.CustomUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


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
    public String createAddress(@ModelAttribute Address address) {
        addressService.save(address);
        return "redirect:/addresses";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        addressService.getById(id).ifPresent(address -> model.addAttribute("address", address));
        model.addAttribute("users", customUserService.getAll());
        return "addresses/edit";
    }

    @PostMapping("/update")
    public String updateAddress(@ModelAttribute Address address) {
        addressService.save(address);
        return "redirect:/addresses";
    }

    @GetMapping("/delete/{id}")
    public String deleteAddress(@PathVariable Long id) {
        addressService.delete(id);
        return "redirect:/addresses";
    }
}
