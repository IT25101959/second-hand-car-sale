package com.carshop.controller;

import com.carshop.model.Customer;
import com.carshop.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/register")
    public String showRegisterPage(Model model) {

        model.addAttribute("customer", new Customer());

        return "customer-register";
    }

    @PostMapping("/register")
    public String registerCustomer(
            @ModelAttribute Customer customer,
            Model model) {

        Customer savedCustomer =
                customerService.registerCustomer(customer);

        if (savedCustomer == null) {
            model.addAttribute("error",
                    "Email already exists!");
            return "customer-register";
        }

        model.addAttribute("success",
                "Registration successful!");

        return "customer-register";
    }
}