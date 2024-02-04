package com.lmp.furniture_store_demo.models.discount_agreements;

import org.springframework.lang.Nullable;

import com.lmp.furniture_store_demo.enums.DiscountAgreementType;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "discount_agreements__percentage_off_product")
public class PercentageOffProduct extends DiscountAgreement {
  private @NotNull @Nullable String productId;
  private @NotNull int percentageAmount; // The percentage amount to be discounted from the product

  public PercentageOffProduct(@Nullable String customerId, @Nullable String productId,
      int percentageAmount) {
    super(customerId, DiscountAgreementType.PERCENTAGE_OFF_PRODUCT);
    this.productId = productId;
    this.percentageAmount = percentageAmount;
  }

  public PercentageOffProduct() {
  }

  public @Nullable String getProductId() {
    return this.productId;
  }

  public int getPercentageAmount() {
    return this.percentageAmount;
  }

  public @Nullable PercentageOffProduct.Embed toEmbed() {
    return new PercentageOffProduct.Embed(this.getCustomerId(), this.getProductId(), this.getPercentageAmount());
  }

  /**
   * Embeddable version of this discount agreement.
   */
  @Embeddable
  public static class Embed extends DiscountAgreement.Embed {
    @Column(name = "percentage_off_product__product_id")
    public @Nullable String productId;

    @Column(name = "percentage_off_product__percentage_amount")
    public int percentageAmount;

    public Embed(@Nullable String customerId, @Nullable String productId, int percentageAmount) {
      super(customerId, DiscountAgreementType.PERCENTAGE_OFF_PRODUCT);
      this.productId = productId;
      this.percentageAmount = percentageAmount;
    }

    public Embed() {
    }

  }
}
