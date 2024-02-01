import type { CustomerDocument } from "../types";
import { useMemo } from "react";
import { useQuery } from "react-query";
import { getCustomersQueryFn } from "../api/customers.ts";
import { useOrderContext } from "../hooks/useOrderContext.tsx";
import { LabelContainer } from "./LabelContainer.tsx";

export const CustomerSelect = () => {
  const { order, functions } = useOrderContext();

  const customerListResult = useQuery({
    queryKey: ["customers"],
    queryFn: getCustomersQueryFn,
  });

  const customerOptions: {
    label: string;
    value: string;
  }[] = useMemo(() => {
    if (customerListResult.isLoading || customerListResult.isError) {
      return [];
    }

    return customerListResult.data.map((n: CustomerDocument) => ({
      label: n.name,
      value: n.id,
    }));
  }, [customerListResult]);

  const inputTitle = useMemo(() => {
    if (order.customerId) {
      return (
        customerOptions.find((n) => n.value === order.customerId)?.label || ""
      );
    }

    return "Select a customer for which this order is.";
  }, [order.customerId, customerOptions]);

  return (
    <LabelContainer label="Select customer">
      <select
        className="custom-input"
        value={order.customerId ?? ""}
        onChange={(event) => functions.setCustomerId(event.currentTarget.value)}
        title={inputTitle}
      >
        <option value="" disabled>
          -- Select a customer --
        </option>
        {customerOptions.map((n, index) => (
          <option key={`${n.value}-${index}`} value={n.value}>
            {n.label}
          </option>
        ))}
      </select>
    </LabelContainer>
  );
};
