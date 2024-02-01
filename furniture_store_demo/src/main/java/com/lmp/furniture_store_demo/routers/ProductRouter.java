package com.lmp.furniture_store_demo.routers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.lmp.furniture_store_demo.controllers.ProductController;
import com.lmp.furniture_store_demo.models.Product;

@RestController
public class ProductRouter {
  @Autowired
  private ProductController productController;

  @GetMapping("/products")
  public @ResponseBody List<Product> getProducts() {
    return productController.getProducts();
  }

  // Not used in demo
  @GetMapping("/products/{id}")
  public @ResponseBody Product getProduct(@PathVariable String id) throws ResponseStatusException {
    if (id == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id can't be null");
    }

    Product product = productController.getProduct(id);

    if (product == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
    }

    return product;
  }
}
