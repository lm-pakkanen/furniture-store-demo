package com.lmp.furniture_store_demo.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import com.lmp.furniture_store_demo.controllers.database.CustomerRepository;
import com.lmp.furniture_store_demo.models.Customer;

@Component
public class CustomerController {
  @Autowired
  private CustomerRepository customerRepository;

  public CustomerController() {
  }

  public @NonNull List<Customer> getCustomers() {
    List<Customer> customers = new ArrayList<Customer>();
    this.customerRepository.findAll().forEach(customers::add);
    return customers;
  }

  public @Nullable Customer getCustomer(@NonNull String id) {
    return this.customerRepository.findById(id).orElse(null);
  }
}
