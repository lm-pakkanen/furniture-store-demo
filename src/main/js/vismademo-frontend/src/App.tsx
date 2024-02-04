import { IntlProvider } from "react-intl";
import { QueryClient, QueryClientProvider } from "react-query";
import { Navigation } from "./components/Navigation.tsx";
import { OrderContextProvider } from "./contexts/OrderContext.tsx";
import "./app.css";

export interface App {
  children: React.ReactNode;
}

const queryClient = new QueryClient();

export const App = ({ children }: App) => {
  return (
    <IntlProvider locale="en">
      <OrderContextProvider>
        <QueryClientProvider client={queryClient}>
          <main className="layout">
            <section className="layout-children">{children}</section>
            <Navigation />
          </main>
        </QueryClientProvider>
      </OrderContextProvider>
    </IntlProvider>
  );
};
