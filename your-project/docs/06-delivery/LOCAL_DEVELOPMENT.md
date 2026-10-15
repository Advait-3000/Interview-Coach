# Local Development Setup

## Install
1. Install a JDK 21 distribution.
2. Install Maven and confirm `mvn -version` uses Java 21.
3. Install a supported MySQL Server release and start the service.
4. Install an IDE if desired (IntelliJ IDEA or Eclipse); Maven commands remain the source of truth.
5. Clone/open the repository.

## Database setup
1. Use a privileged local MySQL account to create database `interview_coach`.
2. Create a dedicated app user restricted to this database.
3. Run `database/schema.sql`, then synthetic `database/seed.sql`.
4. Copy `.env.example` to a local ignored configuration file and fill in credentials.
5. Confirm the connection using the app's diagnostic/smoke test.

Never expose the MySQL port to the public internet. Do not use real credentials in screenshots or Git.

## Build and test
```bash
mvn clean test
```

## Run
```bash
mvn javafx:run
```
These commands require the corresponding Maven JavaFX plugin and `App` entry point to be implemented.

## Troubleshooting
- `mvn` not found: install Maven or use the IDE's Maven integration.
- Wrong Java version: inspect `mvn -version` and `JAVA_HOME`.
- Connection refused: confirm MySQL service and host/port.
- Access denied: check database username/password and grants.
- Unknown database: create it and run schema SQL.
- JavaFX runtime/module error: verify `pom.xml` plugin/module configuration and JDK compatibility.
- Empty question list: run seed SQL and confirm seeded questions are active.

## First demo
Use a synthetic student account, practise a few questions, complete a short mock test, inspect the saved result/history, and demonstrate admin question creation/deactivation.
