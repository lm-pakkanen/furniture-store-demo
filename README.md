# Furniture store

## Running demo locally

Tested working environment versions:

- Java 21
- Node.js v18.17.1
- npm 9.6.7

### Backend

Run `mvn spring-boot:run`.
Port: 8080.
h2 database interface available at http://localhost:8080/h2-console (url: `jdbc:h2:mem:furniture-store-demo-db`, username: `sa`, password: none)

### Frontend

Run `npm run dev` while in the `src/main/js/furniture-store-demo-frontend/` directory.
Port: 5173.

## Architecture

Fairly standard MVC architecture with CRUD endpoints for the frontend.

## Design choices

### Controllers

Rest controllers (routers) are treated as separate from Controllers to maintain separation of concerns -- these could be combined with Controllers as well.

### Database / entities

All database entities extend a base class for handling ID generation (`models/DatabaseDocument`).

Some entities use embedded data to save e.g. customer information which might change in the database over time.
The embedded documents' idea is to persist their state at the time of the order so that if the original data
changes, the persisted document still has the original data.

### Product class

The product class here is a simple class.
In a more complex application it could be an extendable class to allow for custom properties on various products.

## Known issues

- Embedded documents don't get persisted entirely correctly as embeds don't support inheritance strategies.
