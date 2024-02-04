package com.lmp.furniture_store_demo.models.discount_agreements;

import org.springframework.lang.Nullable;

import com.lmp.furniture_store_demo.enums.DiscountAgreementType;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "discount_agreements__product_combo")
public class ProductCombo extends DiscountAgreement {
  private @NotNull @Nullable String productId;
  private @NotNull int buyCount; // How many products need to be bought to get the discount
  private @NotNull int payCount; // How many products need to be paid for when the discount is applied

  public ProductCombo(@Nullable String customerId, @Nullable String productId, int buyCount, int payCount) {
    super(customerId, DiscountAgreementType.PRODUCT_COMBO);
    this.productId = productId;
    this.buyCount = buyCount;
    this.payCount = payCount;
  }

  public ProductCombo() {
  }

  public @Nullable String getProductId() {
    return this.productId;
  }

  public int getBuyCount() {
    return this.buyCount;
  }

  public int getPayCount() {
    return this.payCount;
  }

  public @Nullable ProductCombo.Embed toEmbed() {
    return new ProductCombo.Embed(this.getCustomerId(), this.getProductId(), this.getBuyCount(), this.getPayCount());
  }

  /**
   * Embeddable version of this discount agreement.
   */
  @Embeddable
  public static class Embed extends DiscountAgreement.Embed {
    @Column(name = "product_combo__product_id")
    public @Nullable String productId;

    @Column(name = "product_combo__buy_count")
    public int buyCount;

    @Column(name = "product_combo__pay_count")
    public int payCount;

    public Embed(@Nullable String customerId, @Nullable String productId, int buyCount, int payCount) {
      super(customerId, DiscountAgreementType.PRODUCT_COMBO);
      this.productId = productId;
      this.buyCount = buyCount;
      this.payCount = payCount;
    }

    public Embed() {
    }
  }
}
