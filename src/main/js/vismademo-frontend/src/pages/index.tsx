import type { ProductDocument } from "../types";
import { useCallback, useMemo } from "react";
import { useQuery } from "react-query";
import { getProductsQueryFn } from "../api/products.ts";
import { ProductCard } from "../components/ProductCard.tsx";
import { useOrderContext } from "../hooks/useOrderContext.tsx";
import "../styles/index-page.css";

export const Index = () => {
  const { order, functions } = useOrderContext();
  const { setProductQuantity } = functions;

  const productsListResult = useQuery({
    queryKey: ["products"],
    queryFn: getProductsQueryFn,
  });

  const handleAddProductClick: ProductCard["handleAddProductClick"] =
    useCallback(
      async (product, quantity) => {
        const currentProductQuantity =
          order.orderRows.find((n) => n.productId === product.id)?.quantity ??
          0;

        const updatedProductQuantity = currentProductQuantity + quantity;
        return await setProductQuantity(product.id, updatedProductQuantity);
      },
      [order.orderRows, setProductQuantity]
    );

  const products: ProductDocument[] = useMemo(() => {
    if (productsListResult.isLoading || productsListResult.isError) {
      return [];
    }

    return productsListResult.data;
  }, [productsListResult]);

  return (
    <article className="index-page-container">
      {products.map((n, index) => (
        <ProductCard
          key={`${n.name}-${index}`}
          product={n}
          handleAddProductClick={handleAddProductClick}
        />
      ))}
    </article>
  );
};
