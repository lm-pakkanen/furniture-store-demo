package com.lmp.furniture_store_demo.controllers.database;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.lmp.furniture_store_demo.models.orders.OrderRow;

@Component
public interface OrderRowRepository extends CrudRepository<OrderRow, String> {
}