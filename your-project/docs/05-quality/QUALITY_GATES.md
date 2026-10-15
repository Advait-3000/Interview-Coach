# Quality Gates

A milestone cannot be marked complete unless its relevant gates pass.

## Build gate
- Maven build succeeds from a clean checkout.
- No missing dependency or JavaFX module errors.
- No secrets in tracked files.

## Code gate
- No SQL in JavaFX controllers.
- Prepared statements used for parameterized SQL.
- Errors are handled and logged safely.
- New business logic has unit tests.

## Data gate
- Foreign keys and unique constraints are present.
- Attempt submission is transactional.
- Student ownership is enforced.
- Seed data is synthetic.

## UX gate
- Loading, empty, success, and error states exist for the relevant screen.
- Keyboard navigation and visible focus work.
- Confirmation exists for final submission/discard actions.

## Release gate
- `mvn clean test` passes.
- Fresh setup instructions have been followed on a clean environment.
- Demo path has been rehearsed.
- Known limitations are documented.
