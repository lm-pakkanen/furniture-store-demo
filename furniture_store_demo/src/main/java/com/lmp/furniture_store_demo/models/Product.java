package com.lmp.furniture_store_demo.models;

import org.springframework.lang.Nullable;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

/**
 * Product class with id, name and price.
 */
@Entity
@Table(name = "products")
public class Product extends DatabaseDocument {
  private @NotNull @Nullable String name;
  private @NotNull int priceInEuroCents;

  public Product(@Nullable String name, int priceInEuroCents) {
    this.name = name;
    this.priceInEuroCents = priceInEuroCents;
  }

  public Product() {
  }

  public @Nullable String getName() {
    return this.name;
  }

  public int getPriceInEuroCents() {
    return this.priceInEuroCents;
  }
}
