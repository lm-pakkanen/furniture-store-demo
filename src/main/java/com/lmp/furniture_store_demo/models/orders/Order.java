package com.lmp.furniture_store_demo.models.orders;

import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.lmp.furniture_store_demo.models.Customer;
import com.lmp.furniture_store_demo.models.DatabaseDocument;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

/**
 * A particular customer's order with order rows.
 * Due to using a decoupled frontend, this model should always be re-created
 * when making changes to the order. Thus customerId and orderRows are final.
 */
@Entity
@Table(name = "orders")
public class Order extends DatabaseDocument implements Cloneable {
  // Unix timestamp of when the order was created (in the frontend).
  private @NotNull @Nullable Long createdAtUnix;

  // Unix timestamp of when the order was placed (created).
  private @Nullable Long orderPlacedAtUnix;

  // Customer whose order this is.
  private @NotNull @Nullable String customerId;

  @Embedded
  private Customer.Embed customerEmbed;

  // List of order rows in this order.
  @ElementCollection
  @CollectionTable(name = "order_rows")
  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
  @JsonManagedReference
  private @NotNull List<OrderRow> orderRows;

  public Order(@NonNull Long createdAtUnix, @NonNull String customerId, @Nullable List<OrderRow> orderRows) {
    this.createdAtUnix = createdAtUnix;
    this.customerId = customerId;
    this.orderRows = orderRows;
  }

  public Order() {
  }

  @Override
  public @NonNull Order clone() throws CloneNotSupportedException {
    Order clonedOrder = (Order) super.clone();

    if (clonedOrder == null) {
      throw new CloneNotSupportedException();
    }

    return clonedOrder;
  }

  public @Nullable Long getCreatedAtUnix() {
    return this.createdAtUnix;
  }

  public @Nullable Long getOrderPlacedAtUnix() {
    return this.orderPlacedAtUnix;
  }

  public void setOrderPlacedAtUnix(@Nullable Long orderPlacedAtUnix) {
    this.orderPlacedAtUnix = orderPlacedAtUnix;
  }

  public @Nullable String getCustomerId() {
    return this.customerId;
  }

  public @Nullable Customer.Embed getCustomerEmbed() {
    return this.customerEmbed;
  }

  public void setCustomerEmbed(@Nullable Customer.Embed customerEmbed) {
    this.customerEmbed = customerEmbed;
  }

  public @Nullable List<OrderRow> getOrderRows() {
    return this.orderRows;
  }

  public void setOrderRows(@Nullable List<OrderRow> orderRows) {
    this.orderRows = orderRows;
  }
}
