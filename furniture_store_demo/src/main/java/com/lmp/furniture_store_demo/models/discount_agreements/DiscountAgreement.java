package com.lmp.furniture_store_demo.models.discount_agreements;

import org.springframework.lang.Nullable;

import com.lmp.furniture_store_demo.enums.DiscountAgreementType;
import com.lmp.furniture_store_demo.models.DatabaseDocument;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;

@MappedSuperclass
public class DiscountAgreement extends DatabaseDocument {
  private @NotNull @Nullable String customerId;

  @Enumerated(EnumType.STRING)
  private @NotNull @Nullable DiscountAgreementType type;

  public DiscountAgreement(@Nullable String customerId, @Nullable DiscountAgreementType type) {
    this.customerId = customerId;
    this.type = type;
  }

  public DiscountAgreement() {
  }

  public @Nullable String getCustomerId() {
    return this.customerId;
  }

  public @Nullable DiscountAgreementType getType() {
    return this.type;
  }

  public @Nullable DiscountAgreement.Embed toEmbed() {
    return new DiscountAgreement.Embed(this.getCustomerId(), this.getType());
  }

  /**
   * Embeddable version of a discount agreement.
   * Overridden by subclasses.
   */
  @Embeddable
  public static class Embed {
    @Column(name = "discount_agreement__customer_id")
    public @Nullable String customerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_agreement__type")
    public @Nullable DiscountAgreementType type;

    public Embed(@Nullable String customerId, @Nullable DiscountAgreementType type) {
      this.customerId = customerId;
      this.type = type;
    }

    public Embed() {
    }
  }
}
