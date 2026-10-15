# Architecture

## Overview
Use a layered Java desktop architecture:

`JavaFX View → Controller → Service → Repository (JDBC) → MySQL`

## Layers
- **View:** FXML and JavaFX controls. Displays state and collects user input.
- **Controller:** Connects UI events to services. No SQL or scoring algorithms here.
- **Service:** business rules such as authentication, question selection, scoring, and analytics.
- **Repository/DAO:** SQL queries and mapping rows to Java objects.
- **Model:** simple domain classes such as `User`, `Question`, `Attempt`, and `AttemptAnswer`.
- **Configuration:** reads local database settings from environment/system properties or a local ignored config file.

## Data access
Use JDBC with `PreparedStatement`, try-with-resources, and transactions for multi-row assessment submission. Use a connection factory/provider; never open a new connection from every UI event without a clear lifecycle strategy.

## UI threading
Database operations must not freeze JavaFX's UI thread. Use JavaFX `Task`/`Service` or a small executor for longer operations, then update UI state on the JavaFX thread.

## Boundaries
No HTTP server is needed for this local MVP. Do not introduce Spring Boot or microservices unless requirements change. MySQL is a separate database service, not embedded in the JavaFX app.

## Error boundaries
Repositories translate SQL exceptions into application-level failures where appropriate. Services enforce rules. Controllers show safe, actionable messages. Logs keep technical details without secrets.
