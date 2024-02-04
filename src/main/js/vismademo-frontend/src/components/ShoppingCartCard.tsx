import type { OrderRow, ProductDocument } from "../types";
import { useMemo } from "react";
import { useNumberFormats } from "../hooks/useNumberFormats.ts";
import { useOrderContext } from "../hooks/useOrderContext.tsx";
import { LabelContainer } from "./LabelContainer.tsx";
import "../styles/shopping-cart-card.css";

export interface ShoppingCartCard {
  orderRow: OrderRow;
  product: ProductDocument;
}

export const ShoppingCartCard = ({ orderRow, product }: ShoppingCartCard) => {
  const { formatCurrency } = useNumberFormats();
  const { isLoading, functions } = useOrderContext();

  const pricePerProductInEuroCents = useMemo(() => {
    if (!orderRow.rowPriceInEuroCents) {
      return product.priceInEuroCents;
    }

    return orderRow.rowPriceInEuroCents / orderRow.quantity;
  }, [orderRow, product]);

  // Note: discounted price / used discount could be showed in a better way.

  const pricePerTitle = useMemo(() => {
    if (product.priceInEuroCents === pricePerProductInEuroCents) {
      // Note: does not display meaningful discount for "ProductCombo" discount type.
      return formatCurrency(pricePerProductInEuroCents / 100);
    }

    return `Original price: ${formatCurrency(product.priceInEuroCents / 100)}`;
  }, [pricePerProductInEuroCents, product, formatCurrency]);

  return (
    <article className="shopping-cart-card">
      <section className="shopping-cart-card-section">
        <LabelContainer label="Product">
          <span>{product.name}</span>
        </LabelContainer>
        <LabelContainer label="Price per" side="right">
          <span title={pricePerTitle}>
            {formatCurrency(pricePerProductInEuroCents / 100)}
          </span>
        </LabelContainer>
      </section>
      <section className="shopping-cart-card-section">
        <LabelContainer label="Quantity">
          <input
            className="custom-input"
            type="number"
            value={orderRow.quantity}
            onChange={(event) =>
              functions.setProductQuantity(
                product.id,
                Number.parseInt(event.currentTarget.value)
              )
            }
            min={0}
            disabled={isLoading}
            title="Select product quantity to be added to cart."
            required
          />
        </LabelContainer>
        <LabelContainer label="Order total" side="right">
          <span>
            {orderRow.rowPriceInEuroCents !== undefined
              ? formatCurrency(orderRow.rowPriceInEuroCents / 100)
              : "loading..."}
          </span>
        </LabelContainer>
      </section>
    </article>
  );
};
