# Deployment

## MVP deployment model
Local desktop application on a student's or evaluator's computer, connected to a local MySQL instance or a controlled private database network. No public hosting is required.

## Packaging
- Produce a runnable JavaFX application package or platform-specific installer after the app is stable.
- Test packaging on the actual target OS; JavaFX native components can be platform-sensitive.
- Document the JDK/runtime and MySQL requirements for the package.
- Keep database credentials out of packaged resources.

## Database connectivity warning
A desktop client connecting directly to MySQL is appropriate only for a controlled educational demo. Do not expose a MySQL server publicly or distribute a shared privileged credential. For a public multi-user product, add a secured backend service before deployment.

## Release validation
Follow `RELEASE_CHECKLIST.md`. Record build artifact name, version, operating system, and test result.
