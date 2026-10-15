# Logging and Diagnostics

## Local logging
Use SLF4J with Logback. Default level `INFO`; development can use `DEBUG` when necessary.

## Log
- Application startup/shutdown
- Database connection status without credentials
- Important state transitions (attempt started/submitted)
- Unexpected exceptions with a correlation/reference ID
- Admin question changes with actor ID and question ID, not unnecessary personal details

## Do not log
Passwords, password hashes, DB passwords, full credential-bearing URLs, or full private user records.

## User diagnostics
Provide a safe error message and optionally a reference ID that can be matched to local logs. Logging must not become a substitute for handling errors.
