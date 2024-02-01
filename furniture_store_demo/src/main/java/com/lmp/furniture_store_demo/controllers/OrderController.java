package com.lmp.furniture_store_demo.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import com.lmp.furniture_store_demo.controllers.database.OrderRepository;
import com.lmp.furniture_store_demo.models.Customer;
import com.lmp.furniture_store_demo.models.Product;
import com.lmp.furniture_store_demo.models.discount_agreements.DiscountAgreement;
import com.lmp.furniture_store_demo.models.discount_agreements.PercentageOffOrder;
import com.lmp.furniture_store_demo.models.discount_agreements.PercentageOffProduct;
import com.lmp.furniture_store_demo.models.discount_agreements.ProductCombo;
import com.lmp.furniture_store_demo.models.orders.Order;
import com.lmp.furniture_store_demo.models.orders.OrderRow;

@Component
public class OrderController {

  @Autowired
  private OrderRepository orderRepository;

  private CustomerController customerController;
  private ProductController productController;
  private DiscountAgreementController discountAgreementController;

  public OrderController() {
  }

  /**
   * Injects controller dependency, used to avoid possible circular dependencies
   * between controllers
   * 
   * @param customerController
   */
  @Autowired
  public void setCustomerController(CustomerController customerController) {
    this.customerController = customerController;
  }

  /**
   * Injects controller dependency, used to avoid possible circular dependencies
   * between controllers
   * 
   * @param productController
   */
  @Autowired
  public void setProductController(ProductController productController) {
    this.productController = productController;
  }

  /**
   * Injects controller dependency, used to avoid possible circular dependencies
   * between controllers
   * 
   * @param discountAgreementController
   */
  @Autowired
  public void setDiscountAgreementController(DiscountAgreementController discountAgreementController) {
    this.discountAgreementController = discountAgreementController;
  }

  /**
   * Gets all orders.
   */
  public @NonNull List<Order> getOrders() {
    List<Order> orders = new ArrayList<Order>();
    this.orderRepository.findAll().forEach(orders::add);
    return orders;
  }

  /**
   * Gets an order by id.
   * 
   * @param id
   */
  public @Nullable Order getOrder(@NonNull String id) {
    return this.orderRepository.findById(id).orElse(null);
  }

  /**
   * Saves an order.
   * 
   * @param order
   * @throws CloneNotSupportedException
   * @throws IllegalArgumentException
   */
  public @NonNull Order saveOrder(@NonNull Order order)
      throws CloneNotSupportedException, IllegalArgumentException {
    String customerId = order.getCustomerId();

    if (customerId == null) {
      throw new IllegalArgumentException("Can't save an order without a customer id.");
    }

    Customer customer = this.customerController.getCustomer(customerId);

    if (customer == null) {
      throw new IllegalArgumentException("Customer not found.");
    }

    // Add customer embed, order place time
    order.setCustomerEmbed(customer.toEmbed());
    order.setOrderPlacedAtUnix(System.currentTimeMillis() / 1000L);

    Order orderWithPrices = this.getOrderWithPrices(order);

    // Get order rows from order with prices
    List<OrderRow> orderRows = orderWithPrices.getOrderRows();
    orderRows = orderRows != null ? orderRows : new ArrayList<OrderRow>();

    // Reset order row's references to order & vise versa
    orderRows.forEach((orderRow) -> orderRow.setOrder(orderWithPrices));
    orderWithPrices.setOrderRows(orderRows);

    Order savedOrder = this.orderRepository.save(orderWithPrices);
    return savedOrder;
  }

  /**
   * Updates an order's order rows to include prices and returns a cloned order.
   * 
   * Calculates the whole order's prices at once -- could be improved to calculate
   * only the changed row's prices
   * 
   * @param order
   * @throws CloneNotSupportedException
   * @throws IllegalArgumentException
   */
  public @NonNull Order getOrderWithPrices(@NonNull Order order)
      throws CloneNotSupportedException, IllegalArgumentException {
    // Clone order to avoid mutation
    Order newOrder = order.clone();

    List<OrderRow> orderRows = newOrder.getOrderRows();
    orderRows = orderRows != null ? orderRows : new ArrayList<OrderRow>();

    List<DiscountAgreement> customerDiscountAgreements = discountAgreementController
        .getDiscountAgreementsByCustomerId(newOrder.getCustomerId());

    List<OrderRow> orderRowsWithPrices = new ArrayList<OrderRow>();

    for (OrderRow orderRow : orderRows) {
      String productId = orderRow.getProductId();
      Integer quantity = orderRow.getQuantity();

      if (productId == null || quantity == null) {
        throw new IllegalArgumentException("Can't calculate prices for an order row because a required value is null.");
      }

      if (quantity == 0) {
        // Note: Could be improved to remove the row from the list or throw an error to
        // avoid confusing interface
        continue;
      }

      Product product = this.productController.getProduct(productId);

      if (product == null) {
        throw new IllegalArgumentException("Product not found.");
      }

      List<DiscountAgreement> productDiscountAgreements = OrderController
          .getDiscountAgreementsForProduct(customerDiscountAgreements, productId);

      DiscountAgreement usedDiscountAgreement = null;
      Integer usedProductPrice = null;

      for (DiscountAgreement agreement : productDiscountAgreements) {
        int agreementProductPrice = DiscountAgreementController.getDiscountedProductPriceInEuroCents(product, agreement,
            quantity);

        // No previously calculated price or this price is lower
        if (usedProductPrice == null || agreementProductPrice < usedProductPrice) {
          usedProductPrice = agreementProductPrice;
          usedDiscountAgreement = agreement;
        }
      }

      if (usedProductPrice == null) {
        // No discount agreement found, use default price
        usedProductPrice = DiscountAgreementController.getDiscountedProductPriceInEuroCents(product, null, quantity);
      }

      orderRow.setRowPriceInEuroCents(usedProductPrice);

      if (usedDiscountAgreement != null) {
        // Note: somewhat arbitrarily / non-deterministically chooses discount if
        // multiple discounts amount to the same product price
        orderRow.setDiscountAgreementEmbed(usedDiscountAgreement.toEmbed());
      }

      orderRowsWithPrices.add(orderRow);
    }

    newOrder.setOrderRows(orderRowsWithPrices);
    return newOrder;
  }

  /**
   * Filter out customer's discount agreements by product id.
   * Does not filter out agreements that apply to all
   * products (e.g. PercentageOffOrder).
   * 
   * @param discountAgreements
   * @param productId
   */
  private static @NonNull List<DiscountAgreement> getDiscountAgreementsForProduct(
      @NonNull List<DiscountAgreement> discountAgreements,
      @NonNull String productId) {
    List<DiscountAgreement> productDiscountAgreements = new ArrayList<DiscountAgreement>();

    for (DiscountAgreement agreement : discountAgreements) {
      // PercentageOffOrder applies to all products
      if (agreement instanceof PercentageOffOrder) {
        productDiscountAgreements.add(agreement);
        continue;
      }

      // Add PercentageOffProduct if product id matches
      if (agreement instanceof PercentageOffProduct) {
        String agreementProductId = ((PercentageOffProduct) agreement).getProductId();

        if (agreementProductId != null && agreementProductId.equals(productId)) {
          productDiscountAgreements.add(agreement);

        }

        continue;
      }

      // Add ProductCombo if product id matches
      if (agreement instanceof ProductCombo) {
        // This could probably be combined with the PercentageOffProduct case, perhaps
        // via a common interface for getting the product id.
        String agreementProductId = ((ProductCombo) agreement).getProductId();

        if (agreementProductId != null && agreementProductId.equals(productId)) {
          productDiscountAgreements.add(agreement);
        }

        continue;
      }
    }

    return productDiscountAgreements;
  }
}
