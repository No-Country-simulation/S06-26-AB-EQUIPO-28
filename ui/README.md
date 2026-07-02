# App BiT -- Public Data Panel for Social Inclusion

A responsive web application with an AI-powered agent that consolidates public data enriched with the Visent CDRView dataset -- person concentration and network coverage data by region -- and answers natural-language queries to guide digital inclusion and social equity policies.

## Problem

Public managers lack easy access to cross-referenced data on mobility, employment, and mental health by region. Decisions about social programs are often based on intuition rather than evidence. App BiT solves this by providing a decision tool that helps public managers understand where, how, and why inequalities occur -- so they can act before they deepen.

**Challenge reference:** [documentation/help.md](documentation/help.md)

## Data Sources

The core dataset is **Visent CDRView** -- synthetic urban mobility data generated from a proprietary framework that replicates real mobile network behavioral patterns. The data covers the Metropolitan Region of Florianopolis, SC, Brazil.

**Key files:**

| File | Description |
|------|-------------|
| `tensor_mobilidade.csv` | 16.8M mobility events from 200K subscribers over 15 days |
| `tensor_concentracao.csv` | Person concentration by antenna, day, and time period |
| `tensor_od.csv` | Origin-destination pairs between geographic clusters |
| `tensor_sequencias.csv` | Ordered antenna sequence per subscriber per day |
| `tensor_fluxo_vias.csv` | Consecutive antenna pairs with user volume and flow percentage |
| `tensor_tempo_deslocamento.csv` | Inter-cluster distances |
| `trajetos_comuns.csv` | K-anonymized OD pairs (K=3) |
| `antenas_flp.csv` | 132 real Claro ERBs (Anatel) geocoded in the metro region |
| `assinantes.csv` | Demographic profiles of all 200K synthetic subscribers |

**Technical reference:** [CDRView_AppBit_TechnicalReference_v2.md](documentation/CDRView_AppBit_TechnicalReference_v2.md)

**Privacy:** All data is synthetic. K-anonymity is set to K=3 for the hackathon (K=5 mandatory for production per LGPD Art. 12).

## Prerequisites

- **Java 17+** (backend)
- **Node.js 18+** (frontend)
- **PostgreSQL** (database)
- **OpenRouter API key** (for AI assistant)

## Setup Instructions

### Backend (Spring Boot)

```bash
cd appbit/api

# 1. Configure environment variables
#    Copy .env.example to .env and fill in your values

# 2. Run Flyway migrations to set up the database schema
#    (handled automatically on startup, or run manually via IDE)

# 3. Start the server
./mvnw spring-boot:run
```

The API runs at `http://localhost:8080`. Scalar API docs are available at `/docs`.

### Frontend (React + Vite)

```bash
cd appbit/ui

npm install
npm run dev
```

The app runs at `http://localhost:5173`.

## Environment Variables

| Variable | Description | Example |
|----------|-------------|---------|
| `OPENROUTER_API_KEY` | API key from OpenRouter | `sk-or-...` |
| `OPENROUTER_MODEL` | Model to use for AI responses | `google/gemini-2.0-flash-001` |
| `SPRING_DATASOURCE_URL` | PostgreSQL connection URL | `jdbc:postgresql://localhost:5432/appbit` |
| `SPRING_DATASOURCE_USERNAME` | Database user | `postgres` |
| `SPRING_DATASOURCE_PASSWORD` | Database password | `your_password` |

## API Endpoints

| Method | Path | Description |
|--------|------|-------------|
| `GET` | `/api/v1/map/regions` | Regions aggregated by cluster with concentration and connectivity metrics |
| `POST` | `/api/v1/data/query` | Natural-language query in challenge format (consulta, filtros, idioma) |
| `GET` | `/api/v1/inclusion/health-report` | Mental health vulnerability report by period |
| `GET` | `/api/v1/inclusion/vulnerable-regions` | Regions with high social and digital vulnerability |
| `POST` | `/api/v1/assistant/queries` | AI assistant query endpoint |
| `GET` | `/api/v1/demographics` | Citizen demographic profiles (dev profile only) |

## Architecture

### Frontend: Feature-Sliced Design (FSD) + Functional DDD

The frontend follows Feature-Sliced Design, organizing code into strict layers with one-way dependencies:

- `app/` -- Global configuration, router, providers
- `pages/` -- Route-level views (Dashboard, Map)
- `widgets/` -- Complex UI blocks (AI Query Dashboard, Interactive Map)
- `features/` -- User interactions (ask-ai-query, filter-by-region, export-report)
- `entities/` -- Business domain types (mobility-data, region, indicator)
- `shared/` -- Generic UI components, API clients, utilities

Business entities are represented as immutable TypeScript types. Logic lives in custom hooks following functional DDD principles. Hexagonal architecture (Ports and Adapters) decouples UI from backend APIs.

### Backend: DDD + Hexagonal Architecture + CQRS

The backend follows Domain-Driven Design with Hexagonal Architecture:

- **Bounded contexts:** AI Assistant, Telemetry, Inclusion Core, Demographics
- **Ports:** Abstract interfaces that define contracts
- **Adapters:** Implementations that translate between protocols (REST, DB, AI)
- **Use Cases:** Orchestrated by internal endpoints, not controllers
- **CQRS:** Commands and queries are separated for clarity

## How to Use

1. Open the application in your browser
2. Select a region from the dropdown
3. View the interactive map showing person concentration and network coverage data
4. Use the AI Query bar to ask questions in natural language (e.g., "Where are there gaps in mental health program coverage?")
5. Receive a structured response with data, sources, and visualization
6. Refine your query or export the report

**Example queries:**
- "Which regions have high population concentration but poor network coverage?"
- "Where should mental health programs be prioritized based on connectivity gaps?"
- "What is the employment rate versus person concentration in Florianopolis clusters?"

## Project Structure

```
appbit/
  api/                          # Spring Boot backend
    app-bit-service/
      src/main/java/tech/nocountry/talent/appbitservice/
        aiassistant/            # AI assistant bounded context
        telemetry/              # Mobility data and map bounded context
        inclusioncore/          # Health vulnerability bounded context
        demographics/           # Citizen profiles bounded context
        shared/                 # Cross-cutting concerns
  ui/                           # React + Vite frontend
    src/
      app/                      # Global config, router, providers
      pages/                    # Route-level views
      widgets/                  # Complex UI blocks
      features/                 # User interactions
      entities/                 # Domain types
      shared/                   # Generic components and utilities
```

## Team

Built by the NoCountry hackathon team as part of the Soberania Digital & App BiT challenge.

**Partners:** Visent, Wongola, Angola Cables, Oracle, PMI-SP

---

*Data is synthetic. No real subscriber or operator data was used. Antenna locations sourced from public Anatel data.*
