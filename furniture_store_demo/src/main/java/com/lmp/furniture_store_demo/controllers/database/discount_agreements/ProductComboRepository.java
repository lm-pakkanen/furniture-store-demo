package com.lmp.furniture_store_demo.controllers.database.discount_agreements;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.lmp.furniture_store_demo.models.discount_agreements.ProductCombo;

import jakarta.persistence.Table;

@Component
@Table(name = "discount_agreement__product_combo")
public interface ProductComboRepository extends CrudRepository<ProductCombo, String> {
}