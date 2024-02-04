package com.lmp.furniture_store_demo.controllers.database.discount_agreements;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.lmp.furniture_store_demo.models.discount_agreements.PercentageOffOrder;

import jakarta.persistence.Table;

@Component
@Table(name = "discount_agreement__percentage_off_order")
public interface PercentageOffOrderRepository extends CrudRepository<PercentageOffOrder, String> {
}