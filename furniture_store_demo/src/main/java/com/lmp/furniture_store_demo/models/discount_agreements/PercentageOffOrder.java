package com.lmp.furniture_store_demo.models.discount_agreements;

import org.springframework.lang.Nullable;

import com.lmp.furniture_store_demo.enums.DiscountAgreementType;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "discount_agreements__percentage_off_order")
public class PercentageOffOrder extends DiscountAgreement {
  private @NotNull int percentageAmount; // The percentage amount to be discounted from the order

  public PercentageOffOrder(@Nullable String customerId, int percentageAmount) {
    super(customerId, DiscountAgreementType.PERCENTAGE_OFF_ORDER);
    this.percentageAmount = percentageAmount;
  }

  public PercentageOffOrder() {
  }

  public int getPercentageAmount() {
    return this.percentageAmount;
  }

  public @Nullable PercentageOffOrder.Embed toEmbed() {
    return new PercentageOffOrder.Embed(this.getCustomerId(), this.getPercentageAmount());
  }

  /**
   * Embeddable version of this discount agreement.
   */
  @Embeddable
  public static class Embed extends DiscountAgreement.Embed {
    @Column(name = "percentage_off_order__percentage_amount")
    public int percentageAmount;

    public Embed(@Nullable String customerId, int percentageAmount) {
      super(customerId, DiscountAgreementType.PERCENTAGE_OFF_ORDER);
      this.percentageAmount = percentageAmount;
    }

    public Embed() {
    }
  }
}
