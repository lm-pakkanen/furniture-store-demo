package com.lmp.furniture_store_demo;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.lmp.furniture_store_demo.controllers.database.CustomerRepository;
import com.lmp.furniture_store_demo.controllers.database.ProductRepository;
import com.lmp.furniture_store_demo.controllers.database.discount_agreements.PercentageOffOrderRepository;
import com.lmp.furniture_store_demo.controllers.database.discount_agreements.PercentageOffProductRepository;
import com.lmp.furniture_store_demo.controllers.database.discount_agreements.ProductComboRepository;
import com.lmp.furniture_store_demo.models.Customer;
import com.lmp.furniture_store_demo.models.Product;
import com.lmp.furniture_store_demo.models.discount_agreements.PercentageOffOrder;
import com.lmp.furniture_store_demo.models.discount_agreements.PercentageOffProduct;
import com.lmp.furniture_store_demo.models.discount_agreements.ProductCombo;

@SpringBootApplication
public class FurnitureStoreDemoApplication {

  public static void main(String[] args) {
    SpringApplication.run(FurnitureStoreDemoApplication.class, args);
  }

  /**
   * Initialize the in-memory database with some mock data.
   * Creates various discount schemas for customers.
   * 
   * @param customerRepository
   * @param productRepository
   * @param percentageOffOrderRepository
   * @param percentageOffProductRepository
   * @param productComboRepository
   */
  @Bean
  public ApplicationRunner init(CustomerRepository customerRepository,
      ProductRepository productRepository,
      PercentageOffOrderRepository percentageOffOrderRepository,
      PercentageOffProductRepository percentageOffProductRepository,
      ProductComboRepository productComboRepository) {
    return args -> {
      Customer customer1 = customerRepository.save(new Customer("Customer Doe inc."));
      Customer customer2 = customerRepository.save(new Customer("Customer Doe ltd."));
      Customer customer3 = customerRepository.save(new Customer("Customer doe llc."));

      Product product1 = productRepository.save(new Product("Chair", 5_000)); // 50eur
      Product product2 = productRepository.save(new Product("Sofa", 15_000)); // 150eur
      Product product3 = productRepository.save(new Product("Table", 12_000)); // 120eur

      // 'Customer Doe inc.' gets 20% off on all products
      percentageOffOrderRepository.save(new PercentageOffOrder(customer1.getId(), 20));
      // 'Customer Doe inc.' gets 10% off the 'Sofa' product
      percentageOffProductRepository.save(new PercentageOffProduct(customer1.getId(), product2.getId(), 10));
      // 'Customer Doe inc.' gets 30% off the 'Table' product
      percentageOffProductRepository.save(new PercentageOffProduct(customer1.getId(), product3.getId(), 30));
      // 'Customer Doe ltd.' gets 20% off the 'Sofa' product
      percentageOffProductRepository.save(new PercentageOffProduct(customer2.getId(), product2.getId(), 20));
      // 'Customer doe llc.' gets 3 chairs for the price of 2
      productComboRepository.save(new ProductCombo(customer3.getId(), product1.getId(), 3, 2));
    };
  }
}
