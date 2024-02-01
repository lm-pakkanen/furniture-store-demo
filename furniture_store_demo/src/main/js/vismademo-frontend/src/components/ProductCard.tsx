import type { ProductDocument } from "../types";
import { useCallback, useState } from "react";
import { useNumberFormats } from "../hooks/useNumberFormats.ts";
import { useOrderContext } from "../hooks/useOrderContext.tsx";
import { LabelContainer } from "./LabelContainer.tsx";
import "../styles/product-card.css";

export interface ProductCard {
  product: ProductDocument;
  handleAddProductClick: (
    product: ProductDocument,
    quantity: number
  ) => Promise<boolean>;
}

export const ProductCard = ({
  product,
  handleAddProductClick,
}: ProductCard) => {
  const { formatCurrency } = useNumberFormats();
  const { order } = useOrderContext();

  const [quantity, setQuantity] = useState(1);

  const _handleAddProductClick = useCallback(async () => {
    const isSuccess = await handleAddProductClick(product, quantity);

    if (isSuccess) {
      // Reset quantity to 1 after adding product to cart.
      setQuantity(1);
    }
  }, [handleAddProductClick, product, quantity]);

  return (
    <article className="product-card">
      <section className="product-card-section">
        <LabelContainer label={product.name}>
          <span>{formatCurrency(product.priceInEuroCents / 100)}</span>
        </LabelContainer>
      </section>
      <section className="product-card-section">
        <LabelContainer label="Quantity">
          <input
            className="custom-input product-card-input"
            type="number"
            value={quantity}
            onChange={(event) =>
              setQuantity(Number.parseInt(event.currentTarget.value))
            }
            min={1}
            title="Select product quantity to add to cart."
            required
          />
        </LabelContainer>
        <button
          className="custom-button custom-button-main product-card-button"
          onClick={_handleAddProductClick}
          disabled={!order.customerId}
          title={
            order.customerId
              ? "Add product to cart"
              : "Please select a customer before adding products to cart."
          }
        >
          Add to cart
        </button>
      </section>
    </article>
  );
};
