import { useContext } from "react";
import { OrderContext } from "../contexts/OrderContext.tsx";

export const useOrderContext = () => useContext(OrderContext);
