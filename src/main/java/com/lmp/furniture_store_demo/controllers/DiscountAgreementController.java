package com.lmp.furniture_store_demo.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.lmp.furniture_store_demo.controllers.database.discount_agreements.PercentageOffOrderRepository;
import com.lmp.furniture_store_demo.controllers.database.discount_agreements.PercentageOffProductRepository;
import com.lmp.furniture_store_demo.controllers.database.discount_agreements.ProductComboRepository;
import com.lmp.furniture_store_demo.enums.DiscountAgreementType;
import com.lmp.furniture_store_demo.models.Customer;
import com.lmp.furniture_store_demo.models.Product;
import com.lmp.furniture_store_demo.models.discount_agreements.DiscountAgreement;
import com.lmp.furniture_store_demo.models.discount_agreements.PercentageOffOrder;
import com.lmp.furniture_store_demo.models.discount_agreements.PercentageOffProduct;
import com.lmp.furniture_store_demo.models.discount_agreements.ProductCombo;

import io.micrometer.common.lang.Nullable;

/**
 * This controller contains most of the logic of the given task.
 * If this was part of a larger application, most of the logic here could be
 * encapsulated in the order module / controller.
 * 
 * Due to also creating a frontend for this demo, I've decided to keep the logic
 * here.
 */

@Component
public class DiscountAgreementController {
  @Autowired
  private PercentageOffOrderRepository percentageOffOrderRepository;

  @Autowired
  private PercentageOffProductRepository percentageOffProductRepository;

  @Autowired
  private ProductComboRepository productComboRepository;

  private CustomerController customerController;

  public DiscountAgreementController() {
  }

  /**
   * Injects controller dependency, used to avoid possible circular dependencies
   * between controllers
   * 
   * @param customerController
   */
  @Autowired
  private void setCustomerController(CustomerController customerController) {
    this.customerController = customerController;
  }

  /**
   * Gets all discount agreements for a given customer.
   * 
   * @param customerId
   * @throws IllegalArgumentException
   */
  public @NonNull List<DiscountAgreement> getDiscountAgreementsByCustomerId(@Nullable String customerId)
      throws IllegalArgumentException {
    if (customerId == null) {
      throw new IllegalArgumentException("Customer id can't be null.");
    }

    Customer customer = customerController.getCustomer(customerId);

    // Make sure customer still exists
    if (customer == null) {
      throw new IllegalArgumentException("Customer not found.");
    }

    // Get all discount agreements
    List<DiscountAgreement> discountAgreements = new ArrayList<DiscountAgreement>();
    percentageOffOrderRepository.findAll().forEach(discountAgreements::add);
    percentageOffProductRepository.findAll().forEach(discountAgreements::add);
    productComboRepository.findAll().forEach(discountAgreements::add);

    // Return customer's agreements
    // Note: In a real-life application filtering would be done in the database /
    // more efficiently
    List<DiscountAgreement> filteredDiscountAgreements = discountAgreements.stream().filter(agreement -> {
      String agreementCustomerId = agreement.getCustomerId();
      return agreementCustomerId != null && agreementCustomerId.equals(customerId);
    }).toList();

    return filteredDiscountAgreements == null ? new ArrayList<DiscountAgreement>() : filteredDiscountAgreements;
  }

  /**
   * Gets discounted price for a given DiscountAgreement and Product.
   * If there is no discount agreement for a given product, returns the original
   * price of the product.
   * 
   * It's debatable whether this method should belong to
   * DiscountAgreementController or ProductController.
   * I chose to include it here as it's closely related to discount agreements.
   * 
   * @param product
   * @param discountAgreement Agreement to use or null if no applicable agreement
   *                          is found
   * @param quantity
   */
  public static int getDiscountedProductPriceInEuroCents(@NonNull Product product,
      @Nullable DiscountAgreement discountAgreement,
      int quantity) {

    int standardPrice = ProductController.getProductPrice(product, quantity);

    // If no agreement is supplied, return standard price
    if (discountAgreement == null) {
      return standardPrice;
    }

    DiscountAgreementType agreementType = discountAgreement.getType();

    // If no agreement type is supplied, return standard price
    if (agreementType == null) {
      return standardPrice;
    }

    switch (agreementType) {
      // Apply percentage discount
      case DiscountAgreementType.PERCENTAGE_OFF_ORDER: {
        return DiscountAgreementController.getPercentageOffOrderPrice(
            (PercentageOffOrder) discountAgreement, standardPrice);
      }

      // Apply percentage discount if product id matches
      case DiscountAgreementType.PERCENTAGE_OFF_PRODUCT: {
        return DiscountAgreementController.getPercentageOffProductPrice(product,
            (PercentageOffProduct) discountAgreement, standardPrice);
      }

      // Apply product combo discount
      case DiscountAgreementType.PRODUCT_COMBO: {
        return DiscountAgreementController.getComboProductPrice(product, (ProductCombo) discountAgreement,
            standardPrice, quantity);
      }

      // Note: New agreement types are easy to add, just add a new case here.

      // Unknown agreement type, return default price
      // Note: this could also throw an exception if required by the program context
      default: {
        return standardPrice;
      }
    }
  }

  /**
   * Calculates the price of a product given a PercentageOffOrder agreement and a
   * standard price.
   * 
   * @param percentageOffOrderAgreement
   * @param standardPrice
   */
  private static int getPercentageOffOrderPrice(
      @NonNull PercentageOffOrder percentageOffOrderAgreement, int standardPrice) {
    int discountPercentageAmount = percentageOffOrderAgreement.getPercentageAmount();
    return standardPrice * (100 - discountPercentageAmount) / 100;
  }

  /**
   * Calculates the price of a product given a PercentageOffProduct agreement and
   * a standard price. Returns the standard price if the product id does not match
   * the agreement's product id.
   * 
   * @param product
   * @param percentageOffProductAgreement
   * @param standardPrice
   */
  private static int getPercentageOffProductPrice(@NonNull Product product,
      @NonNull PercentageOffProduct percentageOffProductAgreement, int standardPrice) {
    int discountPercentageAmount = 0;

    String agreementProductId = percentageOffProductAgreement.getProductId();

    if (agreementProductId != null && agreementProductId.equals(product.getId())) {
      discountPercentageAmount = percentageOffProductAgreement.getPercentageAmount();
    }

    return standardPrice * (100 - discountPercentageAmount) / 100;
  }

  /**
   * Calculates the price of a product given a ProductCombo agreement and a
   * standard price.
   * 
   * @param product
   * @param productComboAgreement
   * @param standardPrice
   * @param quantity
   */
  private static int getComboProductPrice(@NonNull Product product, @NonNull ProductCombo productComboAgreement,
      int standardPrice, int quantity) {
    String agreementProductId = productComboAgreement.getProductId();

    // If product id does not match, return standard price
    if (agreementProductId != null && !agreementProductId.equals(product.getId())) {
      return standardPrice;
    }

    int payCount = productComboAgreement.getPayCount();
    int buyCount = productComboAgreement.getBuyCount();

    // How many times the agreement is fulfilled, e.g. 'buy 3, pay 2' is fulfilled
    // twice if quantity is at least 6
    //
    // This assumes that this discount can be applied multiple times, e.g.
    // 'buy 3, pay 2' should yield 5 products if quantity is 7,
    // 4 products if quantity is 6 and 2 products if quantity is 3
    int agreementFulfilledCount = Math.floorDiv(quantity, buyCount);

    // 'buy 3, pay 2' -> 3 - 2 = 1
    int quantityDifference = buyCount - payCount;

    // Remove 'quantityDifference' amount of products from the total quantity, e.g.
    // 'buy 3, pay 2', quantity = 7 -> 7 - (2 * (3 - 2)) = 5
    int discountedQuantity = quantity - (agreementFulfilledCount * quantityDifference);

    // Get price with 'discounted' quantity
    return ProductController.getProductPrice(product, discountedQuantity);
  }
}
