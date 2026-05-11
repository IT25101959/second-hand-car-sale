package com.carshop.service;

import com.carshop.model.Customer;
import com.carshop.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public Customer registerCustomer(Customer customer) {

        Customer existingCustomer =
                customerRepository.findByEmail(customer.getEmail());

        if (existingCustomer != null) {
            return null;
        }

        return customerRepository.save(customer);
    }
}