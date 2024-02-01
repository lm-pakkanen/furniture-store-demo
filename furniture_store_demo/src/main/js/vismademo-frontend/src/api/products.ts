/**
 * Gets products as a request, to be used by react-query
 */
export const getProductsQueryFn = async (): Promise<any> => {
  const result = await fetch("http://localhost:8080/products");
  return await result.json();
};
