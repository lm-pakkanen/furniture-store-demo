import { useCallback } from "react";
import { useIntl } from "react-intl";

export interface UseNumberFormats {
  formatCurrency: (value: number) => string;
}

export const useNumberFormats = (): UseNumberFormats => {
  const { formatNumber: intlFormatNumber } = useIntl();

  /**
   * Formats a number as a currency value in the current locale.
   * Uses "en" locale as default.
   */
  const formatCurrency: UseNumberFormats["formatCurrency"] = useCallback(
    (value) => {
      return intlFormatNumber(value, {
        style: "currency",
        currency: "EUR",
        currencyDisplay: "symbol",
        maximumFractionDigits: 2,
        minimumFractionDigits: 2,
      });
    },
    [intlFormatNumber]
  );

  return {
    formatCurrency,
  };
};
