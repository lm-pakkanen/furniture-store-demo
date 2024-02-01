package com.lmp.furniture_store_demo.routers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.lmp.furniture_store_demo.controllers.OrderController;
import com.lmp.furniture_store_demo.models.orders.Order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
public class OrderRouter {
  @Autowired
  private OrderController orderController;

  /**
   * Calculates prices for a given order.
   * Used by frontend to display prices in real time.
   * Placed at the top due to greedy matching in path.
   * 
   * @param order
   * @throws ResponseStatusException
   */
  @PostMapping("/orders/get-prices")
  public @ResponseBody Order getPrices(@RequestBody @Valid Order order) throws ResponseStatusException {
    if (order == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order can't be null");
    }

    try {
      return this.orderController.getOrderWithPrices(order);
    } catch (CloneNotSupportedException exception) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to get order with prices");
    }

  }

  // Not used in demo
  @GetMapping("/orders/{id}")
  public @ResponseBody Order getOrder(@PathVariable @NotNull String id) throws ResponseStatusException {
    if (id == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id can't be null");
    }

    Order order = orderController.getOrder(id);

    if (order == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
    }

    return order;
  }

  @PostMapping("/orders")
  public @ResponseBody Order createOrder(@RequestBody @Valid Order order) throws ResponseStatusException {
    if (order == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order can't be null");
    }

    try {
      return this.orderController.saveOrder(order);
    } catch (CloneNotSupportedException exception) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save order");
    }
  }

  // Not used in demo, could be used to update an order
  // (e.g. replace order with new instance)
  @PutMapping("/orders/{id}")
  public @ResponseBody Order replaceOrder(@PathVariable String id, @RequestBody @Valid Order order)
      throws ResponseStatusException {
    if (id == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id can't be null");
    }

    if (order == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order can't be null");
    }

    try {
      return this.orderController.saveOrder(order);
    } catch (CloneNotSupportedException exception) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save order");
    }
  }
}
