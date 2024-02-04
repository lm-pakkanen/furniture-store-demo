package com.lmp.furniture_store_demo.models;

import org.springframework.lang.Nullable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "customers")
public class Customer extends DatabaseDocument {
  private @NotNull @Nullable String name;

  public Customer(@Nullable String name) {
    this.name = name;
  }

  public Customer() {
  }

  public @Nullable String getName() {
    return this.name;
  }

  public @Nullable Customer.Embed toEmbed() {
    return new Customer.Embed(this.getName());
  }

  /**
   * Embeddable version of a customer.
   */
  @Embeddable
  public static class Embed {
    @Column(name = "customer__name")
    public @Nullable String name;

    public Embed(@Nullable String name) {
      this.name = name;
    }

    public Embed() {
    }
  }
}
