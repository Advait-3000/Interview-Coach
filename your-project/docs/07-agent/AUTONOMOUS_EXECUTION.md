# Autonomous Execution Policy

## Allowed without asking
- Create source files required by the current approved task.
- Add unit tests and synthetic fixtures.
- Refactor local code to maintain the documented architecture.
- Update docs and non-destructive build configuration.
- Run ordinary builds and tests.

## Ask first
- Change language, UI framework, database, or architecture.
- Add paid APIs or send data outside the machine.
- Add features outside the MVP that increase scope.
- Delete user data or run destructive database operations.
- Change credentials, access controls, or admin bootstrap behavior in a way that may lock out users.
- Overwrite substantial existing work.

## Stop conditions
Stop and report if the environment is missing required tools, the build cannot be made reproducible, security boundaries are unclear, or acceptance criteria conflict. Do not simulate test success.
