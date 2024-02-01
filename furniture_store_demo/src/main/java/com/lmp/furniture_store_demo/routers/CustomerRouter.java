package com.lmp.furniture_store_demo.routers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.lmp.furniture_store_demo.controllers.CustomerController;
import com.lmp.furniture_store_demo.models.Customer;

import jakarta.validation.constraints.NotNull;

@RestController
public class CustomerRouter {
  @Autowired
  private CustomerController customerController;

  public CustomerRouter() {
  }

  // Not used in demo
  @GetMapping("/customers")
  public @ResponseBody List<Customer> getCustomers() {
    return this.customerController.getCustomers();
  }

  @GetMapping("/customers/{id}")
  public @ResponseBody Customer getCustomer(@PathVariable @NotNull String id) {
    if (id == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id can't be null");
    }

    Customer customer = this.customerController.getCustomer(id);

    if (customer == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
    }

    return customer;
  }
}
