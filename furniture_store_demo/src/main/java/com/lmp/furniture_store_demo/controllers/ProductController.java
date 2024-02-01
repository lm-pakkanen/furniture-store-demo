package com.lmp.furniture_store_demo.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import com.lmp.furniture_store_demo.controllers.database.ProductRepository;
import com.lmp.furniture_store_demo.models.Product;

@Component
public class ProductController {
  @Autowired
  private ProductRepository productRepository;

  public ProductController() {
  }

  /**
   * Gets all products.
   */
  public @NonNull List<Product> getProducts() {
    List<Product> products = new ArrayList<Product>();
    this.productRepository.findAll().forEach(products::add);
    return products;
  }

  /**
   * Gets a product by id.
   * 
   * @param id
   */
  public @Nullable Product getProduct(@NonNull String id) {
    return this.productRepository.findById(id).orElse(null);
  }

  /**
   * Get product price without any discounts (quantity * price).
   * 
   * @param product
   * @param quantity
   */
  public static int getProductPrice(@NonNull Product product, int quantity) throws IllegalArgumentException {
    if (quantity <= 0) {
      throw new IllegalArgumentException("Quantity must be greater than 0.");
    }

    return product.getPriceInEuroCents() * quantity;
  }
}
