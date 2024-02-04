import React from "react";
import ReactDOM from "react-dom/client";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import { App } from "./App.tsx";
import { Index } from "./pages/index.tsx";

ReactDOM.createRoot(document.getElementById("root")!).render(
  <React.StrictMode>
    <App>
      <BrowserRouter>
        <Routes>
          <Route index element={<Index />} />
        </Routes>
      </BrowserRouter>
    </App>
  </React.StrictMode>
);
