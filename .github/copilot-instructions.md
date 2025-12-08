## Repo Overview

- Project: Quizzer — a Spring Boot backend with a React (Vite + TypeScript) frontend located in `student-frontend`.
- Backend uses Java 21, Spring Boot (Web, Data JPA, Thymeleaf), H2 for development/tests and PostgreSQL in production.

## High-level architecture (big picture)

- Backend: Spring Boot monolith. Packages grouped by feature under `src/main/java/com/example/quizzer/` (e.g. `quiz`, `question`, `category`, `answeroption`, `review`, `studentanswer`).
- Layers inside each feature folder follow a simple service/controller/repository pattern: `*Controller` (web endpoints), `*Service` (business logic), `*Repository` (Spring Data JPA).
- Persistence: JPA entities in each feature package (e.g. `Quiz`, `Question`, `AnswerOption`); repository interfaces extend `JpaRepository`.
- Views: Thymeleaf templates in `src/main/resources/templates` are used by the server-rendered teacher dashboard. The React app is a separate SPA used for student-facing UI in `student-frontend`.
- Data flow: Frontend (port 5173) calls backend REST endpoints under `/api/*`. Backend controllers typically return DTOs or views for Thymeleaf templates.

## Key files and locations (fast references)

- Application entry: `src/main/java/com/example/quizzer/QuizzerApplication.java` (Spring Boot main). Root redirect to `/quizzes` is implemented there.
- Backend build: `pom.xml` (Maven). Java version set to 21.
- Tests: `src/test/*` using an in-memory H2 DB configured by `src/test/resources/application.properties`.
- Templates: `src/main/resources/templates/*` (server-side pages like `quiz-list.html`, `add-question.html`).
- Frontend app: `student-frontend/` (Vite + React + TypeScript). Services live in `student-frontend/src/services/`.

## Build / run / test commands (concrete)

- Backend (recommended via wrapper):
  - Windows (PowerShell): ``./mvnw.cmd test`` or ``./mvnw.cmd spring-boot:run``
  - macOS / Linux: ``./mvnw test`` or ``./mvnw spring-boot:run``
  - Package (skip tests): ``./mvnw.cmd -DskipTests package`` (Windows)
- Frontend:
  - Install: `npm install` in `student-frontend`
  - Dev server: `npm run dev` (default port 5173)
  - Build: `npm run build`

## Project-specific conventions & patterns

- Feature packaging: code is organized by feature (not layer-first). When adding functionality, create new classes under the feature package rather than a global `service` or `controller` package.
- Naming:
  - Controllers: `*Controller` or `*RestController`
  - Services: `*Service`
  - Repositories: `*Repository` (extend `JpaRepository<T, Long>`)
  - DTOs: live under `dto` package and are used by REST endpoints
- Template vs API: teacher admin UI uses server-side Thymeleaf templates (files in `templates/`), while the student UI uses the React SPA calling `/api/*`. Do not conflate the two rendering approaches when modifying controllers.
- Entity relationships: Many entities rely on JPA mappings (e.g. `Quiz` -> `Question` with `cascade = CascadeType.ALL` and `orphanRemoval=true`). Be careful when deleting entities — check cascades and student answer counts (see `StudentAnswerRepository.countByQuizId`).

## Tests and CI considerations

- Unit & integration tests run with the Maven build and use an in-memory H2 DB. Running `./mvnw test` will pick up test DB config from `src/test/resources/application.properties`.
- When changing schema or entities, update tests and ensure H2 mappings remain compatible. The project uses JPA and Hibernate defaults; small schema changes frequently break tests.

## Common edits patterns and examples

- Adding a new REST endpoint for a feature `X`:
  1. Add entity under `src/main/java/com/example/quizzer/x/` if needed.
  2. Add `XRepository` (extend `JpaRepository`).
  3. Add `XService` for logic and persistence orchestration.
  4. Add a controller `XRestController` under the same package exposing `/api/x...` endpoints.
  5. Add DTOs under `dto/` and map request/response payloads.

- Example: look at `QuizRepository`, `QuizService`, `QuizRestController` to follow naming + layering conventions.

## Integrations and runtime behavior

- Production DB: PostgreSQL. Use `application.properties` and `application-rahti.properties` for environment-specific differences. See `target/classes/application-rahti.properties` for deployed defaults.
- OpenAPI: `springdoc-openapi` is included — Swagger UI is available when the app is running (check the current springdoc path).

## What an AI agent should prioritize when making changes

- Preserve existing URL shapes (`/api/...`) and Thymeleaf view names unless explicitly changing routes.
- Respect transactional boundaries: prefer service-layer changes for business logic; controllers should remain thin.
- When touching entity mappings or delete logic, search for repository queries like `countByQuizId` (student answers) to avoid breaking safe-delete rules.
- Use existing helper services (e.g. `CategoryService`) rather than directly querying repositories from controllers.

## Developer workflows (for a human to verify changes an agent makes)

- Quick local verification steps after backend changes:
  1. `./mvnw.cmd -DskipTests package` (Windows) to compile and produce an artifact.
  2. `./mvnw.cmd spring-boot:run` then visit `http://localhost:8080` and the H2 console at `/h2-console`.
  3. Run `./mvnw.cmd test` to make sure unit/integration tests pass.
- To verify frontend changes:
  1. `cd student-frontend`
  2. `npm install` (once)
  3. `npm run dev` and open `http://localhost:5173` to test the SPA against the running backend.

## Safety and caution

- Do not remove or alter server-side Thymeleaf templates without confirming which controller/view depends on them.
- Be cautious when changing the public API paths — update the React services in `student-frontend/src/services/` accordingly.

## If you need more context

- Inspect examples in `src/main/java/com/example/quizzer/quiz/` and `student-frontend/src/services/` for paired backend + frontend request/response shapes.
- For DB schema expectations, check entity classes (e.g. `Quiz`, `Question`, `AnswerOption`) for field names and relationships.

---

If you'd like, I can iterate this file to include additional examples (specific controller + DTO snippets) or merge with any existing instructions you want preserved. What should I add next?
