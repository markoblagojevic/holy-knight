# Holy Knight

Holy Knight is a full-stack turn-based RPG game.

The project focuses on:
- server-side gameplay logic
- clean backend architecture
- configurable game content
- responsive UI
- scalable battle systems

The frontend acts mainly as a visual client, while all gameplay logic is handled on the backend.

---

# Features

- Turn-based combat system
- Multiple enemy encounters
- Dynamic encounter progression
- XP and level progression
- Learnable monster abilities
- Move equipment management
- Buff and debuff effects
- Healing and damage effects
- Dynamic battle log
- Config-driven gameplay system
- Swagger/OpenAPI integration
- Docker support
- Responsive browser UI

---

# Tech Stack

## Backend

- Java 17
- Spring Boot
- Spring Web
- Lombok
- Jackson
- Springdoc OpenAPI / Swagger

## Frontend

- HTML
- CSS
- Vanilla JavaScript
- Fetch API

## DevOps

- Docker
- Docker Compose

---

# Architecture

The backend follows a layered architecture:

```text
Controller
↓
Service
↓
Battle Engine / Factory / Strategy
↓
Domain Model
```

## Main Responsibilities

### Controllers
Expose REST endpoints to the frontend.

### Services
Handle gameplay flow and application orchestration.

### Battle Engine
Contains all combat rules and turn resolution logic.

### Run Factory
Creates and initializes new game runs.

### Strategy System
Controls monster move selection and enemy behavior.

### DTO Layer
Separates internal domain objects from API responses.

### Config Loader
Loads game content from JSON configuration files.

---

# Gameplay Flow

```text
Start Run
→ View Encounter Map
→ Start Battle
→ Select Hero Move
→ Monster Responds
→ Win or Lose
→ Learn Enemy Move
→ Equip Moves
→ Continue Progression
```

The player progresses through a sequence of encounters.  
After defeating monsters, the hero gains XP and learns new abilities from enemies.

---

# Config-Driven Gameplay

Game data is loaded from:

```text
src/main/resources/game-config.json
```

The following can be modified without changing gameplay logic:

- hero stats
- hero moves
- monster stats
- monster moves
- XP rewards
- AI strategy names

This separates data from logic and makes balancing significantly easier.

---

# API Documentation

Swagger UI:

```text
http://localhost:8080/swagger-ui/index.html
```

Main endpoints:

```text
POST /api/runs
GET  /api/runs/{runId}
PUT  /api/runs/{runId}/equip-moves

POST /api/battles/{runId}/start
POST /api/battles/{runId}/turn
GET  /api/battles/{runId}

POST /api/admin/reload-config
```

---

# Run with Docker

From the project root:

```bash
docker compose up --build
```

Application:

```text
http://localhost:8080
```

---

# Run Locally

```bash
./mvnw spring-boot:run
```

Application:

```text
http://localhost:8080
```

---

# Project Highlights

## Server-Side Battle Engine

All combat calculations are performed on the backend.  
The frontend only sends actions and renders the updated game state.

## Strategy Pattern for Monster AI

Enemy behavior is separated from combat execution logic, making monster AI easier to extend.

## Config-Driven Design

Gameplay content is dynamically loaded from JSON instead of being hardcoded.

## DTO Architecture

DTOs and mappers separate internal domain models from external API contracts.

## Dockerized Setup

The project can be started using Docker Compose for easy setup and reproducibility.

---

# Screenshots

## Main Menu

_Add screenshot here_

## Battle Screen

_Add screenshot here_

## Move Management

_Add screenshot here_

## Victory Screen

_Add screenshot here_

---

# Future Improvements

- Save/load system
- Database persistence
- Boss encounters
- Inventory system
- Procedural branching map
- Audio effects
- More advanced enemy AI
- Expanded visual effects
- Multiplayer support

---

# Author

Marko Blagojevic