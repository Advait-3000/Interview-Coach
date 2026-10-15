# Setup Notes

## Environment observed during implementation
- Java: JDK 24 is installed at `C:/Program Files/Java/jdk-24`. The project targets Java 21; JDK 24 is expected to remain compatible for source/target 21.
- Maven: not available on PATH and not found in common install locations. Needs to be installed before `mvn` commands can be used.
- MySQL: MySQL Server 8.0 binaries exist at `C:/Program Files/MySQL/MySQL Server 8.0/bin/`. The server process is running locally.
- MySQL auth: the `root` account did not authenticate with an empty password or with `1234`. The working credentials have not yet been identified.

## Current task state
This project started as documentation-only. No application code has been implemented yet.

## Files created before environment cleanup
- `.env.example`
- `.env`
- `.gitignore`
- `database/schema.sql`
- `docs/06-delivery/SETUP_NOTES.md`

These files exist so implementation can continue immediately once Maven and MySQL credentials are available.

## Next environment step
Obtain one of the following before schema creation and application build:
- MySQL credentials for a user with privileges on `interview_coach`, or
- A MySQL account that can create the database and app user, plus the app user credentials for `.env`.

Do not run destructive MySQL commands or assume an empty password.
