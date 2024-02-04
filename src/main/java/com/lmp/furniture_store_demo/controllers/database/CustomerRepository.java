
package com.lmp.furniture_store_demo.controllers.database;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.lmp.furniture_store_demo.models.Customer;

@Component
public interface CustomerRepository extends CrudRepository<Customer, String> {
}