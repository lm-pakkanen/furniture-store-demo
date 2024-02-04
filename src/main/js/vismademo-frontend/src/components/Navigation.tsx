import type { ProductDocument } from "../types";
import { useMemo } from "react";
import { useQuery } from "react-query";
import { useNumberFormats } from "../hooks/useNumberFormats.ts";
import { useOrderContext } from "../hooks/useOrderContext.tsx";
import { CustomerSelect } from "./CustomerSelect.tsx";
import { LabelContainer } from "./LabelContainer.tsx";
import { ShoppingCartCard } from "./ShoppingCartCard.tsx";
import "../styles/navigation.css";

export const Navigation = () => {
  const { formatCurrency } = useNumberFormats();
  const { order, functions } = useOrderContext();

  const selectedProductIds = useMemo(
    () => order.orderRows.map((n) => n.productId),
    [order.orderRows]
  );

  const productsListResult = useQuery({
    queryKey: ["products", selectedProductIds],
    queryFn: async () => {
      const resultPromises: Promise<Response>[] = selectedProductIds.map((n) =>
        fetch(`http://localhost:8080/products/${n}`)
      );

      // Await responses in parallel
      const results = await Promise.all(resultPromises);

      // Await response JSONs in parallel
      return Promise.all(results.map((n) => n.json()));
    },
  });

  const products: ProductDocument[] = useMemo(() => {
    if (productsListResult.isLoading || productsListResult.isError) {
      return [];
    }

    return productsListResult.data as ProductDocument[];
  }, [productsListResult]);

  const orderTotalInEuroCents: null | number = useMemo(() => {
    if (order.orderRows.some((n) => !n.rowPriceInEuroCents)) {
      return null;
    }

    return order.orderRows.reduce(
      (acc, product) => acc + product.rowPriceInEuroCents!,
      0
    );
  }, [order.orderRows]);

  return (
    <nav className="navigation">
      <section className="navigation-section">
        <CustomerSelect />
      </section>
      <section className="navigation-section">
        <LabelContainer label="Products in cart">
          {products.length === 0 && <span>No products in cart</span>}
          <div className="navigation-products-section">
            {order.orderRows.map((orderRow, index) => {
              const product = products.find((n) => n.id === orderRow.productId);

              if (!product) {
                return null;
              }

              return (
                <ShoppingCartCard
                  key={`${orderRow.productId}-${index}`}
                  orderRow={orderRow}
                  product={product}
                />
              );
            })}
          </div>
        </LabelContainer>
        <div className="navigation-section-subsection">
          <LabelContainer label="Order total">
            <span>
              {orderTotalInEuroCents === null
                ? "loading..."
                : formatCurrency(orderTotalInEuroCents / 100)}
            </span>
          </LabelContainer>
          <button
            className="custom-button custom-button-main navigation-submit-order-button"
            onClick={functions.placeOrder}
            disabled={products.length === 0}
            title="Place order"
          >
            Place order
          </button>
        </div>
      </section>
    </nav>
  );
};
