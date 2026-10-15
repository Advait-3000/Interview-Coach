# CI/CD

## MVP pipeline
A GitHub Actions workflow may:
1. Check out code.
2. Set up JDK 21.
3. Cache Maven dependencies.
4. Run `mvn clean test`.
5. Package the application on successful tests.

## Rules
- CI must not require a real student account or production DB.
- Unit tests run on every pull request.
- Integration tests may use a dedicated ephemeral/test MySQL service; never point CI at production.
- Secrets must be configured in repository secret storage and never printed.
- Release packaging can be manual initially; avoid multi-platform packaging complexity until the app works.
- A green build does not replace manual testing of JavaFX UI and target-machine setup.
