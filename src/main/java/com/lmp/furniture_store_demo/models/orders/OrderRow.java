package com.lmp.furniture_store_demo.models.orders;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.lmp.furniture_store_demo.models.DatabaseDocument;
import com.lmp.furniture_store_demo.models.discount_agreements.DiscountAgreement;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

/**
 * Order row, e.g. 2x product A.
 * Optionally contains calculated row price and used discount agreement.
 */
@Entity
@Table(name = "order_rows")
public class OrderRow extends DatabaseDocument {
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "order_id")
  @JsonBackReference
  private @Nullable Order order;

  private @NotNull @Nullable String productId;
  private @NotNull int quantity;
  private @Nullable Integer rowPriceInEuroCents = null;

  @Embedded
  private @Nullable DiscountAgreement.Embed discountAgreementEmbed = null;

  /**
   * Order row without calculated row price
   * 
   * @param productId
   * @param quantity
   */
  public OrderRow(@Nullable String productId, int quantity) {
    this.productId = productId;
    this.quantity = quantity;
  }

  public OrderRow() {
  }

  public @Nullable Order getOrder() {
    return this.order;
  }

  public void setOrder(@NonNull Order order) {
    this.order = order;
  }

  public @Nullable String getProductId() {
    return this.productId;
  }

  public int getQuantity() {
    return this.quantity;
  }

  public @Nullable Integer getRowPriceInEuroCents() {
    return this.rowPriceInEuroCents;
  }

  public void setRowPriceInEuroCents(int rowPriceInEuroCents) {
    this.rowPriceInEuroCents = rowPriceInEuroCents;
  }

  public @Nullable DiscountAgreement.Embed getDiscountAgreementEmbed() {
    return this.discountAgreementEmbed;
  }

  public void setDiscountAgreementEmbed(@Nullable DiscountAgreement.Embed discountAgreementEmbed) {
    this.discountAgreementEmbed = discountAgreementEmbed;
  }
}
