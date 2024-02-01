import type { Order } from "../types";

export const createOrder = async (order: Order): Promise<false | Order> => {
  const result = await fetch("http://localhost:8080/orders", {
    headers: {
      "Content-Type": "application/json",
    },
    method: "POST",
    body: JSON.stringify(order),
  });

  if (result.status !== 200) {
    return false;
  }

  return await result.json();
};

export const getOrderWithPrices = async (
  order: Order
): Promise<false | Order> => {
  const result = await fetch("http://localhost:8080/orders/get-prices", {
    headers: {
      "Content-Type": "application/json",
    },
    method: "POST",
    body: JSON.stringify(order),
  });

  if (result.status !== 200) {
    return false;
  }

  return await result.json();
};
