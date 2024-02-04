/**
 * Gets customers as a request, to be used by react-query
 */
export const getCustomersQueryFn = async (): Promise<any> => {
  const result = await fetch("http://localhost:8080/customers");
  return await result.json();
};
