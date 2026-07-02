# Panel de Datos Públicos — App BiT (B2G) — Equipo 28

> **Simulación No Country — S06-26-AB-EQUIPO-28**  
> Proyecto B2G (Business to Government) — Plataforma de análisis de desigualdad social con datos de movilidad urbana.

---

## Índice

- [Introducción](#introducción)
- [Objetivos](#objetivos)
- [Stack Tecnológico](#stack-tecnológico)
- [Arquitectura General](#arquitectura-general)
- [Frontend — App BiT UI](#frontend--app-bit-ui)
  - [Estructura de Carpetas (FSD)](#estructura-de-carpetas-fsd)
  - [Stack y Dependencias](#stack-y-dependencias)
- [Backend — App BiT API](#backend--app-bit-api)
  - [Estructura de Carpetas (DDD + CQRS)](#estructura-de-carpetas-ddd--cqrs)
  - [Endpoints de la API](#endpoints-de-la-api)
  - [Stack y Dependencias](#stack-y-dependencias-1)
- [Modelo de Datos](#modelo-de-datos)
- [Setup Local](#setup-local)
  - [Requisitos Previos](#requisitos-previos)
  - [Backend](#backend)
  - [Frontend](#frontend)
  - [Docker](#docker)
- [Perfiles de Despliegue](#perfiles-de-despliegue)
- [Enlaces y Recursos](#enlaces-y-recursos)
- [Equipo](#equipo)

---

## Introducción

**App BiT** es una plataforma web pensada para gestores públicos y tomadores de decisiones que necesitan entender dónde, cómo y por qué ocurren las desigualdades sociales. Combina datos de movilidad urbana (dataset sintético Vísent CDRView) con fuentes públicas para ofrecer visualizaciones claras y accionables.

El proyecto se enmarca en una simulación No Country con un stack moderno: **React 19 + TypeScript 6** en el frontend y **Spring Boot 4 + Java 25** en el backend, con **Spring AI + OpenRouter** para el agente conversacional.

---

## Objetivos

1. Traducir bases de datos complejas en visualizaciones claras para gestores públicos.
2. Permitir consultas en lenguaje natural sobre datos de desigualdad social mediante un agente de IA.
3. Cruzar información de infraestructura, empleo, educación, salud y movilidad urbana.
4. Representar geográficamente las brechas sociales mediante un mapa interactivo.
5. Proveer reportes exportables sobre formaciones, empleabilidad, mentorías, salud mental y experiencias estructurantes.

---

## Stack Tecnológico

**Frontend**
- React + TypeScript — React 19, TypeScript 6
- Build: Vite 8 + oxlint
- Mapas: MapLibre GL 5.24
- Testing: Vitest + Testing Library
- HTTP: Axios 1.18, TanStack React Query 5.101

**Backend**
- Spring Boot 4.1 + Java 25
- Base de datos: PostgreSQL con migraciones Flyway 12
- IA: Spring AI 2.0 + OpenRouter
- Seguridad: JWT (jjwt 0.13) + Spring Security
- Documentación API: SpringDoc OpenAPI 3 + Scalar UI
- Testing: JUnit 5 + Testcontainers 1.21 + Spring Boot test slices
- Rate limiting: Bucket4J 0.14
- Mapeo: MapStruct 1.6

**Infraestructura**
- Docker + Docker Compose (PostgreSQL)
- Multi-stage build

---

## Arquitectura General

```text
┌──────────────────────────────────────────────────────────────────┐
│                        FRONTEND (React + Vite)                   │
│                                                                  │
│              Pages → Widgets → Features → Entities               │
│                   (dependencia estricta top-down)                │
│                      ↓                                           │
│                   Shared (UI, API, Lib)                          │
│                                                                  │
│          Puertos y Adaptadores + React Context (DI)              │
│                                                                  │
└──────────────────────────┬───────────────────────────────────────┘
                           │ HTTP (Axios)
                           ▼
┌──────────────────────────────────────────────────────────────────┐
│                     BACKEND (Spring Boot 4)                      │
│                                                                  │
│  ┌───────────────────────────────────────────────────────────┐   │
│  │                 BOUNDED CONTEXTS (DDD)                    │   │
│  │                                                           │   │
│  │                                                           │   │
│  │    ┌───────────────────┐          ┌──────────────────┐    │   │
│  │    │   aiassistant     │          │  inclusioncore   │    │   │
│  │    │  (Orquestador IA) │─────────→│  (CORE DOMAIN)   │    │   │
│  │    └───────────────────┘          └────────┬─────────┘    │   │
│  │                                            │              │   │
│  │  ┌──────────────┐  ┌──────────────┐  ┌────┴───────────┐   │   │
│  │  │  telemetry   │  │ demographics │  │ userandroles   │   │   │
│  │  │  (Soporte)   │  │  (Soporte)   │  │  (Soporte)     │   │   │
│  │  └──────────────┘  └──────────────┘  └────────────────┘   │   │
│  │                                                           │   │
│  │  ┌──────────────────────────────────────────────────────┐ │   │
│  │  │                  shared (Transversal)                │ │   │
│  │  │         JPA config · Security · JWT · CORS           │ │   │
│  │  └──────────────────────────────────────────────────────┘ │   │
│  └───────────────────────────────────────────────────────────┘   │
│                                                                  │
│  Todos los BCs siguen la misma estructura hexagonal (ver abajo)  │
│                                                                  │
│  DDD + CQRS + Arquitectura Hexagonal + Anti-Corruption Layer     │
│                                                                  │
└──────────────────────────┬───────────────────────────────────────┘
                           │
                           ▼
                    ┌────────────────┐
                    │   PostgreSQL   │
                    │   + Flyway     │
                    └────────────────┘
```

**Principios de Diseño**

- **Frontend**: Feature-Sliced Design (FSD) + Arquitectura Hexagonal. Puertos y adaptadores con React Context como contenedor de DI inmutables.
- **Backend**: Domain-Driven Design (DDD) + CQRS + Arquitectura Hexagonal. Casos de Uso Atómicos con separación Command/Query.
- **Anti-Corruption Layer**: Mappers en ambos lados que aíslan el dominio del frontend de los cambios en la API, y viceversa.

---

## Frontend — App BiT UI

**Ubicación**: `appbit/ui/`

### Estructura de Carpetas (FSD)

```text
src/
├── app/                      # Configuración global
│   ├── providers/            # React Context (DI container)
│   ├── router/               # React Router (rutas /mapa, /metodologia)
│   ├── layout/               # Layout principal
│   └── styles/               # Estilos globales
│
├── pages/                    # Vistas a nivel de ruta
│   ├── map-view/             # Página del mapa interactivo
│   └── methodology/          # Página de metodología
│
├── widgets/                  # Bloques complejos de UI
│   ├── ai-query-dashboard/   # Dashboard con consultas IA
│   ├── interactive-map/      # Mapa interactivo (MapLibre GL)
│   └── mental-health-insights/  # Insights de salud mental
│
├── features/                 # Interacciones de usuario
│   ├── ask-ai-query/         # Consulta al agente IA
│   │   ├── model/            # Hook useAskAi
│   │   ├── ui/               # AiSearchInput, AiResponseDisplay
│   │   └── index.ts          # API pública
│   └── filter-by-region/     # Filtro por región
│       ├── model/            # Hook useRegionFilter
│       ├── ui/               # RegionSelect, ActiveRegionBadge
│       └── index.ts
│
├── entities/                 # Núcleo de negocio (DDD Funcional)
│   ├── ai-agent/             # Entidad agente IA (tipos, puerto, mapper)
│   ├── mobility-data/        # Datos de movilidad Vísent (tipos, puerto, mapper)
│   ├── region/               # Entidad región (tipos, puerto, mapper)
│   ├── indicator/            # Indicadores sociales (tipos, puerto, mapper)
│   └── mental-health/        # Salud mental (tipos, puerto, mapper)
│
└── shared/                   # Infraestructura genérica
    ├── api/                  # Cliente HTTP (Axios), TanStack Query Client
    ├── ui/                   # UI Kit: Button, Card, Input, Modal, Table, etc.
    ├── lib/                  # Utilidades: formatters, validators, clsx
    └── test/                 # Test setup
```

### Stack y Dependencias

**Producción**
- React + React DOM 19.2.x — UI Framework
- TypeScript 6.0.x — Tipado estático
- Vite 8.1.x — Build tool
- React Router DOM 7.18.x — Enrutamiento SPA
- MapLibre GL 5.24.x — Mapas vectoriales
- Axios 1.18.x — HTTP client
- TanStack React Query 5.101.x — Caché y sincronización
- React Markdown + Remark GFM 10.x — Renderizado Markdown

**Desarrollo**
- Vitest 4.x + Testing Library 6.x — Tests unitarios
- Oxlint 1.69.x — Linter
- jsdom 29.x — Entorno DOM para tests

---

## Backend — App BiT API

**Ubicación**: `appbit/api/`

### Estructura de Carpetas (DDD + CQRS)

```text
src/main/java/tech/nocountry/talent/appbitservice/
│
├── shared/                          # Infraestructura transversal
│   └── infrastructure/
│       ├── persistence/jpa/         # Configuración JPA, naming strategy
│       └── security/                # JWT, SecurityFilterChain, CORS
│
├── telemetry/                       # Bounded Context: Telemetría
├── demographics/                    # Bounded Context: Demografía
├── inclusioncore/                   # Bounded Context: Inclusión (Core Domain)
├── userandroles/                    # Bounded Context: Usuarios y Roles
├── aiassistant/                     # Bounded Context: Asistente IA
└── ...                              # Todos los BCs siguen la misma estructura
```

Cada Bounded Context sigue EXACTAMENTE esta estructura hexagonal:

```text
<contexto>/
│
├── APPLICATION/                     # Casos de Uso (CQRS)
│   └── internal/
│       ├── commandservices/
│       │   └── usecases/            # Comandos atómicos
│       └── queryservices/
│           └── usecases/            # Consultas atómicas
│
├── DOMAIN/                          # Núcleo del negocio
│   ├── exceptions/                  # Excepciones de dominio
│   └── model/
│       ├── aggregates/              # Raíces de Agregado JPA
│       ├── entities/                # Entidades JPA
│       ├── commands/                # Records (Command DTOs)
│       ├── queries/                 # Records (Query DTOs)
│       ├── events/                  # Records (Eventos de dominio)
│       └── valueobjects/            # Records inmutables (Value Objects)
│
├── INFRASTRUCTURE/                  # Persistencia y framework
│   └── persistence/
│       └── jpa/
│           └── repositories/        # Spring Data JPA Repositories
│
└── INTERFACES/                      # Puertos de entrada/salida
    ├── acl/                         # Anti-Corruption Layer (integraciones externas)
    ├── internal/                    # Endpoints internos (sin HTTP)
    └── rest/                        # API REST
        ├── controllers/             # REST Controllers
        ├── docs/                    # Interfaces OpenAPI (*Docs)
        ├── resources/               # DTOs (Java Records)
        └── transform/               # MapStruct Assemblers
```

### Endpoints de la API

**Autenticación y Usuarios**
- `POST /api/v1/auth/sign-up`
- `POST /api/v1/auth/sign-in`
- `GET /api/v1/users`
- `GET /api/v1/users/{userId}`
- `PATCH /api/v1/users/{userId}`
- `DELETE /api/v1/users/{userId}`
- `GET /api/v1/roles`

**Telemetría y Mapas**
- `GET /api/v1/telemetry/antennas`
- `GET /api/v1/telemetry/antennas/{ecgi}`
- `GET /api/v1/telemetry/concentration`
- `GET /api/v1/map/regions`

**Demografía**
- `GET /api/v1/demographics/citizens`
- `POST /api/v1/demographics/citizens`
- `PUT /api/v1/demographics/citizens/{id}`
- `DELETE /api/v1/demographics/citizens/{id}`

**Inclusión Social**
- `GET /api/v1/inclusion/health-report`
- `GET /api/v1/inclusion/vulnerable-regions`

**Asistente IA**
- `POST /api/v1/assistant/queries`
- `POST /api/v1/data/query`

> Total: **19 endpoints REST** — todos documentados con OpenAPI + Scalar UI.

### Stack y Dependencias

**Core**
- Spring Boot 4.1.0 — Framework base
- Java 25 — Lenguaje
- Spring Security 4.x — Autenticación JWT + autorización
- Spring Data JPA 4.x — Persistencia
- PostgreSQL — Base de datos relacional

**Infraestructura**
- Flyway 12.0.1 — Migraciones de base de datos
- SpringDoc OpenAPI 3.0.1 — Documentación OpenAPI + Scalar UI
- MapStruct 1.6.3 — Mapeo DTO ↔ Entidad
- Lombok 1.18.46 — Reducción de boilerplate
- jjwt 0.13.0 — JSON Web Tokens
- Bucket4J 0.14.0 — Rate limiting

**IA**
- Spring AI 2.0.0 — Agente conversacional
- Spring AI OpenAI/OpenRouter — Modelo de lenguaje
- Spring AI pgvector + RAG — Retrieval Augmented Generation

**Testing**
- JUnit 5 — Test runner
- Testcontainers 1.21.4 — Tests de integración con PostgreSQL real
- Spring Boot Test Slices — @WebMvcTest, @DataJpaTest
- ArchUnit — Tests de arquitectura

---

## Modelo de Datos

El esquema se gestiona con **Flyway** (8 migraciones: `V1` a `V8`). Entidades principales:

- **Antennas**: Datos de antenas de telefonía móvil — ubicación, tecnología, operador.
- **Concentration**: Datos de concentración poblacional por cluster geográfico.
- **Citizens**: Datos demográficos de la población.
- **Users**: Usuarios del sistema, roles y autenticación.
- **Health Reports**: Reportes de vulnerabilidad en salud mental por región.
- **Vulnerable Regions**: Regiones con indicadores compuestos de vulnerabilidad social.

---

## Setup Local

### Requisitos Previos

- **Java 25** (JDK) — para el backend
- **Node.js 22+** — para el frontend
- **Docker + Docker Compose** — para PostgreSQL
- **Maven** (incluye `mvnw` en el proyecto)

### Backend

```bash
cd appbit/api

# Configurar variables de entorno
# Crear archivo .env con:
#   DATABASE_URL=jdbc:postgresql://localhost:5432/app_bit_db
#   DB_USER=postgres
#   DB_PASS=tu_password
#   JWT_SECRET=tu_secreto_jwt
#   OPENROUTER_API_KEY=tu_api_key
#   OPENROUTER_MODEL=openai/gpt-4o

# Iniciar base de datos con Docker
docker compose up -d

# Ejecutar en desarrollo
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# Ejecutar en pre-producción
# (definir variable de entorno: SPRING_PROFILES_ACTIVE=prod)
./mvnw spring-boot:run
```

### Frontend

```bash
cd appbit/ui

# Instalar dependencias
npm install

# Iniciar en desarrollo
npm run dev

# Build para producción
npm run build

# Ejecutar tests
npm test
```

### Docker

```bash
cd appbit/api
docker compose up -d          # Inicia PostgreSQL
docker compose down           # Detiene PostgreSQL
```

---

## Perfiles de Despliegue

**Perfil `dev`** (desarrollo local)
- `show-sql: true` — muestra queries SQL en consola
- Pool Hikari: máximo 5 conexiones
- Log level: DEBUG para el paquete de la app

**Perfil `prod`** (pre-producción / producción)
- `show-sql: false` — oculta queries SQL
- Pool Hikari: máximo 15 conexiones, mínimo 2, timeout 20s
- Log level: INFO para el paquete de la app
- Forward headers strategy: native (para proxies)

El perfil activo se define vía variable de entorno: `SPRING_PROFILES_ACTIVE=prod`

Variables de entorno requeridas en producción:
- `DATABASE_URL` — URL de conexión a PostgreSQL
- `DB_USER` — Usuario de base de datos
- `DB_PASS` — Contraseña de base de datos
- `JWT_SECRET` — Secreto para firmar tokens JWT
- `OPENROUTER_API_KEY` — API Key de OpenRouter
- `OPENROUTER_MODEL` — Modelo de IA a utilizar
- `SERVER_PORT` (opcional, default 8080)

---

## Enlaces y Recursos

- **Repositorio GitHub**: [S06-26-AB-EQUIPO-28](https://github.com/No-Country-simulation/S06-26-AB-EQUIPO-28)
- **Frontend (desarrollo)**: `http://localhost:5173`
- **Backend API (local)**: `http://localhost:8080`
- **Documentación API (Scalar UI)**: `http://localhost:8080/scalar`
- **Documentación OpenAPI**: `http://localhost:8080/v3/api-docs`
- **Base de datos PostgreSQL**: `localhost:5432` (vía Docker)

---

## Equipo

**Equipo 28 — Simulación No Country S06**  
App BiT — Panel de Datos Públicos (B2G)

*Documentación generada el 2 de julio de 2026.*
