# Flight Search App

A full-stack web application to search and view flight offers using the Amadeus REST API. The application allows users to search flights by city or airport, choose currencies, and view prices accordingly. Built with React + TypeScript in the frontend and Spring Boot in the backend, all running via Docker Compose.

---

## Tech Stack

**Frontend**
- React + TypeScript
- Zustand (state management)
- shadcn/ui (UI components)
- Vite (build tool)

**Backend**
- Java 17
- Spring Boot
- Gradle (Groovy DSL)
- Amadeus REST API (direct HTTP, no SDK)
- No database required

**Infrastructure**
- Docker & Docker Compose

---

## Features

### Implemented
- Flight search form with:
  - Origin and destination by IATA code (Airport autocomplete by name)
  - Travel dates
  - Number of adults
  - Currency selection (USD, MXN, EUR)
  - Non-stop flight filtering
    
- Results page with flights displayed in cards
- Price and duration sorting of the results in ascendant or descendant order
- Full flight detail view (segments, amenities, etc.)
- Layovers with stop details

### What could be better
- Round-trip logic
> 🛠️ This project currently supports displaying the details of both departure and return flight, however in the results page, only the departure flight is shown, you have to click and go to details page to see the return flight information.

---

## Architecture

```plaintext
├── backend
│   └── src/main/java/... (Spring Boot project)
│       ├── config/
│       ├── controller/
│       ├── service/
│       ├── model/
│       └── exception/
├── frontend
│   └── src/
│       ├── components/
│       ├── pages/
│       ├── store/
│       └── lib/
└── docker-compose.yml
```

- Backend exposes REST endpoints to interact with Amadeus API.
- Frontend consumes backend via fetch to display flight options.

