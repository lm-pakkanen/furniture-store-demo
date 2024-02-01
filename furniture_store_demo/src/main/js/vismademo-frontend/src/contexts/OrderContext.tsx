import type { Order, OrderContextState, OrderRow } from "../types";
import { createContext, useCallback, useState } from "react";
import { createOrder, getOrderWithPrices } from "../api/orders.ts";

const initialState: OrderContextState = {
  isLoading: false,
  order: {
    createdAtUnix: undefined,
    customerId: undefined,
    orderRows: [],
  },
  functions: {
    setCustomerId: () => undefined,
    setProductQuantity: async () => false,
    placeOrder: async () => false,
  },
};

export const OrderContext = createContext<OrderContextState>(initialState);

export const OrderContextProvider = ({
  children,
}: {
  children: React.ReactNode;
}) => {
  const [isLoading, setIsLoading] = useState(initialState.isLoading);

  const [createdAtUnix, setCreatedAtUnix] = useState(
    initialState.order.createdAtUnix
  );

  const [customerId, _setCustomerId] = useState<undefined | string>(
    initialState.order.customerId
  );

  const [orderRows, _setOrderRows] = useState(initialState.order.orderRows);

  /**
   * Gets prices for new order rows and sets them in state.
   */
  const setOrderRows = useCallback(
    async (
      customerId: string,
      createdAtUnix: number,
      orderRows: OrderRow[]
    ) => {
      // Set createdAtUnix if it's not already set
      setCreatedAtUnix((oldState) => oldState ?? createdAtUnix);

      if (!customerId) {
        alert("Customer ID must be set before adding products.");
        return;
      }

      setIsLoading(true);

      const order: Order = {
        createdAtUnix,
        customerId,
        orderRows,
      };

      const resultOrder = await getOrderWithPrices(order);

      if (!resultOrder) {
        alert("Could not update order prices.");
        return;
      }

      _setOrderRows(resultOrder.orderRows);
      setIsLoading(false);
    },
    []
  );

  /**
   * Sets customerId and updates order rows with prices.
   * Also sets row prices because they depend on the selected customer.
   */
  const setCustomerId: OrderContextState["functions"]["setCustomerId"] =
    useCallback(
      (customerId) => {
        setOrderRows(customerId, createdAtUnix ?? Date.now() / 1000, orderRows);
        _setCustomerId(customerId);
      },
      [orderRows, createdAtUnix, setOrderRows]
    );

  /**
   * Update a product's quantity in the order.
   *
   * If product does not yet exist in order, adds it.
   * Otherwise updates the quantity.
   * Removes the product if quantity is 0.
   */
  const setProductQuantity: OrderContextState["functions"]["setProductQuantity"] =
    useCallback(
      async (productId, quantity) => {
        if (!customerId) {
          alert("Customer ID must be set before changing product quantities.");
          return false;
        }

        const mutableNextState = [...orderRows];

        const existingRowIndex = mutableNextState.findIndex(
          (n) => n.productId === productId
        );

        if (existingRowIndex !== -1 && quantity <= 0) {
          // Remove from order
          mutableNextState.splice(existingRowIndex, 1);
        } else if (existingRowIndex !== -1) {
          // Update quantity
          mutableNextState[existingRowIndex].quantity = quantity;
        } else {
          // Add to order
          mutableNextState.push({ productId, quantity });
        }

        await setOrderRows(
          customerId,
          createdAtUnix ?? Date.now() / 1000,
          mutableNextState
        );
        return true;
      },
      [customerId, createdAtUnix, orderRows, setOrderRows]
    );

  /**
   * Handles actually placing order.
   */
  const placeOrder: OrderContextState["functions"]["placeOrder"] =
    useCallback(async () => {
      if (!customerId) {
        alert("Customer ID must be set before placing order.");
        return false;
      }

      if (orderRows.length === 0) {
        alert("Order must contain at least one product.");
        return false;
      }

      setIsLoading(true);

      const order: Order = {
        createdAtUnix: createdAtUnix!, // 'createdAtUnix' is guaranteed to be set if customerId and/or orderRows are set
        customerId,
        orderRows,
      };

      const resultOrder = await createOrder(order);

      if (!resultOrder) {
        alert("Failed to place order.");
        setIsLoading(false);
        return false;
      }

      // Shows order details as an alert, mainly to aid in testing
      alert(
        `Order placed successfully! Order details:\n\n ${JSON.stringify(
          resultOrder,
          null,
          2
        )}`
      );

      // Reset states after placing order

      _setOrderRows([]);
      _setCustomerId(undefined);
      setCreatedAtUnix(undefined);

      setIsLoading(false);
      return true;
    }, [customerId, orderRows, createdAtUnix]);

  const state: OrderContextState = {
    isLoading,
    order: {
      createdAtUnix,
      customerId,
      orderRows,
    },
    functions: {
      setCustomerId,
      setProductQuantity,
      placeOrder,
    },
  };

  return (
    <OrderContext.Provider value={state}>{children}</OrderContext.Provider>
  );
};
