/**
 * Models
 */

export interface DatabaseDocument {
  id: string;
}

export interface DiscountAgreement {}

export interface CustomerInformation {
  name: string;
}

export interface CustomerDocument extends DatabaseDocument {
  name: string;
  customerInformation: CustomerInformation;
}

export interface ProductDocument extends DatabaseDocument {
  name: string;
  priceInEuroCents: number;
}

export interface OrderRow {
  productId: string;
  quantity: number;
  rowPriceInEuroCents?: number;
  discountAgreement?: DiscountAgreement;
}

export interface Order {
  createdAtUnix: number;
  customerId: string;
  orderRows: OrderRow[];
}

/**
 * Contexts
 */

export interface OrderContextState {
  isLoading: boolean;
  order: {
    createdAtUnix: undefined | Order["createdAtUnix"];
    customerId: undefined | Order["customerId"];
    orderRows: Order["orderRows"];
  };
  functions: {
    setCustomerId: (customerId: string) => void;
    setProductQuantity: (
      productId: string,
      quantity: number
    ) => Promise<boolean>;
    placeOrder: () => Promise<boolean>;
  };
}
