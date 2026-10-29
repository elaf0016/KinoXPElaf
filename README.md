# KinoXP 🎬

![Java CI](https://github.com/elaf0016/KinoXPElaf/actions/workflows/ci.yaml/badge.svg)

KinoXP er et biografsystem, hvor kunder kan se filmforestillinger og reservere billetter.
Projektet er lavet på datamatikeruddannelsen på Erhvervsakademi København (EK).

Ud over selve applikationen har jeg lagt vægt på **DevOps**: projektet kører i Docker,
og hver ændring bliver automatisk bygget og testet med GitHub Actions.

## Funktioner

- Se en liste over filmforestillinger på forsiden
- Klikke på en film og se dens forestillinger
- Reservere billetter til en forestilling
- Se alle reservationer

## Teknologi

| Del | Teknologi |
|-----|-----------|
| Backend | Java 21, Spring Boot 4, Spring Web, Spring Data JPA |
| Database | MySQL 8 (H2 i testene) |
| Frontend | HTML, CSS og JavaScript (statiske sider, der kalder REST API'et) |
| DevOps | Docker (multi-stage build), Docker Compose, GitHub Actions (CI) |

## Kør projektet med Docker (nemmest)

Kræver kun [Docker Desktop](https://www.docker.com/products/docker-desktop/).

```bash
docker compose up --build
```

Åbn derefter <http://localhost:8080> i browseren.

Docker Compose starter to containere:

- `db` – en MySQL-database
- `app` – Spring Boot-applikationen, som venter på, at databasen er klar, før den starter

Stop igen med `docker compose down`.

## Kør projektet lokalt uden Docker

Kræver Java 21+ og en MySQL-server på `localhost:3306`.

```bash
# Windows (PowerShell)
$env:DB_PASSWORD="dit-mysql-password"
./mvnw spring-boot:run
```

Databasen `kinoxp` bliver oprettet automatisk, og testdata (film, sale og forestillinger)
bliver indsat fra `src/main/resources/data.sql`.

Indstillingerne kan ændres med miljøvariabler:

| Variabel | Standard |
|----------|----------|
| `DB_URL` | `jdbc:mysql://localhost:3306/kinoxp?createDatabaseIfNotExist=true` |
| `DB_USERNAME` | `root` |
| `DB_PASSWORD` | (tom) |

## Tests og CI

```bash
./mvnw verify
```

Der er integrationstests af hele REST API'et (`src/test/java/dk/kinoxp/KinoApiTests.java`),
som bl.a. tjekker, at ugyldige reservationer bliver afvist.
Testene bruger en H2-database i hukommelsen, så de kan køre uden MySQL.
GitHub Actions kører den samme kommando ved hvert push og pull request
(se `.github/workflows/ci.yaml`).

## REST API

| Metode | Endpoint | Beskrivelse |
|--------|----------|-------------|
| GET | `/api/forestillinger` | Alle forestillinger |
| GET | `/api/film/{id}` | Én film |
| GET | `/api/film/{filmId}/forestillinger` | Forestillinger for en film |
| GET | `/api/reservationer` | Alle reservationer |
| POST | `/api/reservationer` | Opret en reservation |

Ved fejl svarer API'et med en passende statuskode og en besked, fx:

- `404 Not Found`, hvis filmen eller forestillingen ikke findes
- `400 Bad Request`, hvis navnet mangler, eller antal billetter ikke er mellem 1 og 10

```json
{ "message": "Antal billetter skal være mellem 1 og 10" }
```

## Struktur

```
src/main/java/dk/kinoxp/
├── controller/   REST-controllere
├── entity/       JPA-entiteter: Film, Sal, Forestilling, Reservation
└── repository/   Spring Data-repositories
src/main/resources/
├── static/       HTML, CSS og JavaScript
└── data.sql      Testdata
docs/             User stories, product backlog, domænemodel og refleksion
```

## Udviklingsproces

Projektet er udviklet iterativt ud fra små user stories. Se mappen [`docs`](docs):

- [User stories](docs/user-stories.md)
- [Product backlog](docs/product-backlog.md)
- [Refleksion](docs/reflection.md)

## Lavet af

Elaf Al-Hashimi – datamatikerstuderende på EK
[LinkedIn](https://www.linkedin.com/in/elaf-al-hashimi-5aa320326)
